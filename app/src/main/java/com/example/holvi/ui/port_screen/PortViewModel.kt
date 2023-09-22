package com.example.holvi.ui.port_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import com.example.holvi.utils.PasswordManager.Companion.decrypt
import com.example.holvi.utils.PasswordManager.Companion.encrypt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PortViewModel(
    private val passwordDao: PasswordDao,
    private val db: FirebaseFirestore
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
            val data = passwordDao.getAllPasswords()
            if (data.isEmpty()) {
                _portResult.emit(PortResult.Error("You do not have any password to import!"))
                return@launch
            }
            val pathId = UUID.randomUUID().toString().take(4)
            data.forEachIndexed addEach@{ index, password ->
                db.collection("port$pathId").document(index.toString())
                    .set(password.encrypt(pathId)).isSuccessful.let {
                        if (it.not()) {
                            _portResult.emit(PortResult.Error("Could not import data."))
                            return@addEach
                        }
                    }
            }
            _portResult.emit(PortResult.ImportSuccess(pathId))
        }
    }

    private fun export(pathId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("port$pathId").get().await().let { data ->
                if (data.documents.isEmpty()) {
                    _portResult.emit(PortResult.Error("Such key does not exist!"))
                } else {
                    data.documents.forEach {
                        it.data?.toPassword(pathId)
                            ?.let { password -> passwordDao.addPassword(password) }
                    }
                    _portResult.emit(PortResult.ExportSuccess(""))
                }
            }
        }
    }

    private fun Map<String, Any>.toPassword(key: String): Password {
        return Password(
            id = (this.getValue("id") as Long).toInt(),
            password = this.getValue("password") as String,
            userName = this.getValue("userName") as String,
            siteName = this.getValue("siteName") as String,
        ).decrypt(key)
    }

    private fun Password.encrypt(key: String): Password {
        return this.copy(
            siteName = this.siteName.encrypt(key),
            password = this.password.encrypt(key),
            userName = this.userName.encrypt(key)
        )
    }

    private fun Password.decrypt(key: String): Password {
        return this.copy(
            siteName = this.siteName.decrypt(key),
            password = this.password.decrypt(key),
            userName = this.userName.decrypt(key)
        )
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
}