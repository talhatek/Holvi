package com.tek.password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.ExportPasswordUseCase
import com.tek.database.domain.ExportResult
import com.tek.database.domain.GetAllPasswordsUseCase
import com.tek.database.domain.ImportPasswordUseCase
import com.tek.password.domain.PasswordGeneratorUseCase.Companion.encrypt
import com.tek.password.domain.PasswordGeneratorUseCase.Companion.toPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PortViewModel(
    private val getAllPasswords: GetAllPasswordsUseCase,
    private val addPassword: AddPasswordUseCase,
    private val importPassword: ImportPasswordUseCase,
    private val exportPassword: ExportPasswordUseCase,
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
        viewModelScope.launch(Dispatchers.IO) {
            val data = getAllPasswords()
            if (data.isEmpty()) {
                _portResult.emit(PortResult.Error("You do not have any password to import!"))
                return@launch
            }
            val pathId = UUID.randomUUID().toString().take(4)
            data.forEachIndexed addEach@{ index, password ->
                importPassword.invoke("port$pathId", index.toString(), password.encrypt(pathId))
            }
            _portResult.emit(PortResult.ImportSuccess(pathId))
        }
    }

    private fun export(pathId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            with(exportPassword.invoke("port$pathId")) {
                when (this) {
                    is ExportResult.Error ->
                        _portResult.emit(PortResult.Error("Such key does not exist!"))

                    is ExportResult.Success -> data.forEach {
                        it.toPassword(pathId).let { password -> addPassword.invoke(password) }
                    }.also {
                        PortResult.ExportSuccess("Export Completed!")
                    }
                }
            }
        }
    }
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