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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

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
                import(event.pathId)
            }

            is PortEvent.Export -> {
                export(event.pathId)
            }
        }
    }

    //pathId needed for testing. We cant mock .await() nor any() collectionPath
    private fun import(pathId: String?) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _portResult.emit(PortResult.Error("${ex.message}"))
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            val data = getAllPasswords()
            if (data.isEmpty()) {
                _portResult.emit(PortResult.Error("You do not have any password to import!"))
                return@launch
            }
            val path = pathId ?: generateRandomPathId()
            data.forEachIndexed addEach@{ index, password ->
                val res = importPassword.invoke(
                    pathId = path,
                    index = index.toString(),
                    data = password
                )
                if (pathId == null) {
                    res.await()
                }
            }
            _portResult.emit(PortResult.ImportSuccess(path))
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
                            .let { password ->
                                addEncryptedPassword.invoke(
                                    pathId = pathId,
                                    item = password
                                )
                            }
                    }.also {
                        _portResult.emit(PortResult.ExportSuccess("Export Completed!"))
                    }
                }
            }
        }
    }

    private fun generateRandomPathId() = UUID.randomUUID().toString().take(n = 4)
}

sealed class PortEvent {

    data class Import(val pathId: String?) : PortEvent()

    data class Export(val pathId: String) : PortEvent()

}

sealed class PortResult {

    class ImportSuccess(val id: String) : PortResult()

    class ExportSuccess(val message: String) : PortResult()

    class Error(val message: String) : PortResult()
}