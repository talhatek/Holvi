package com.tek.password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.domain.AddEncryptedPasswordUseCase
import com.tek.database.domain.ExportPasswordUseCase
import com.tek.database.domain.ExportResult
import com.tek.database.domain.GetAllPasswordsUseCase
import com.tek.database.domain.ImportPasswordUseCase
import com.tek.password.domain.PasswordGeneratorUseCase.Companion.toPassword
import com.tek.util.AppDispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PortViewModel(
    private val getAllPasswords: GetAllPasswordsUseCase,
    private val addEncryptedPassword: AddEncryptedPasswordUseCase,
    private val importPassword: ImportPasswordUseCase,
    private val exportPassword: ExportPasswordUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {
    private val _portResult = MutableSharedFlow<PortResult>()
    val portResult
        get() = _portResult.asSharedFlow()

    fun onEvent(event: PortEvent) {
        when (event) {
            is PortEvent.Import -> {
                import()
            }

            is PortEvent.Export -> {
                export(event.pathId)
            }
        }
    }

    private fun import() {
        viewModelScope.launch(appDispatchers.IO) {
            val data = getAllPasswords()
            if (data.isEmpty()) {
                _portResult.emit(PortResult.Error("You do not have any password to import!"))
                return@launch
            }
            val pathId = generateRandomPathId()
            data.forEachIndexed addEach@{ index, password ->
                importPassword.invoke(pathId, index.toString(), password)
            }
            _portResult.emit(PortResult.ImportSuccess(pathId))
        }
    }


    private fun export(pathId: String) {
        viewModelScope.launch(appDispatchers.IO) {
            with(exportPassword.invoke("port$pathId")) {
                when (this) {
                    is ExportResult.Error ->
                        _portResult.emit(PortResult.Error("Such key does not exist!"))

                    is ExportResult.Success -> data.forEach {
                        it.toPassword()
                            .let { password -> addEncryptedPassword.invoke(pathId, password) }
                    }.also {
                        PortResult.ExportSuccess("Export Completed!")
                    }
                }
            }
        }
    }

    private fun generateRandomPathId() = "1111"
}

sealed class PortEvent {

    data object Import : PortEvent()

    class Export(val pathId: String) : PortEvent()

}

sealed class PortResult {

    class ImportSuccess(val id: String) : PortResult()

    class ExportSuccess(val message: String) : PortResult()

    class Error(val message: String) : PortResult()
}