package com.tek.holvi.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tek.holvi.domain.ControlRegistrationUseCase
import com.tek.holvi.domain.RegisterPhoneUseCase
import com.tek.holvi.domain.StoreRegistrationKeyUseCase
import com.tek.holvi.model.Key
import com.tek.util.AppDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AuthenticationViewModel(
    private val controlRegistration: ControlRegistrationUseCase,
    private val registerPhone: RegisterPhoneUseCase,
    private val storeRegistrationKey: StoreRegistrationKeyUseCase,
    appDispatchers: AppDispatchers
) : ViewModel() {
    private val _sqState = MutableStateFlow<SQState>(SQState.Init)
    val sqState
        get() = _sqState.asStateFlow()

    init {
        viewModelScope.launch(appDispatchers.IO) {
            checkKey()
        }
    }

    private suspend fun checkKey() {
        try {
            controlRegistration.invoke()?.let {
                storeRegistrationKey.invoke(it)
            } ?: run {
                Key(id = UUID.randomUUID().toString()).apply {
                    registerPhone.invoke(this)
                    storeRegistrationKey.invoke(this)
                }
            }
            _sqState.emit(SQState.Success)
        } catch (e: FirebaseFirestoreException) {
            _sqState.emit(SQState.Error(e.message ?: "Unknown error"))
        }
    }
}

sealed class SQState {
    data object Success : SQState()
    class Error(val message: String) : SQState()
    data object Init : SQState()
}