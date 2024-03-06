package com.tek.password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddViewModel(
    private val passwordDao: PasswordDao,
    private val passwordGenerator: PasswordGeneratorUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {
    private val _clearInputsSharedFlow = MutableSharedFlow<ClearFocus>()
    val clearInputsSharedFlow = _clearInputsSharedFlow.asSharedFlow()
    private val password = MutableStateFlow("")
    val passwordStateFlow = password.asStateFlow()
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()

    fun addPassword(password: Password) {
        viewModelScope.launch(appDispatchers.IO) {
            val controlPassword = controlPassword(password = password)
            if (controlPassword) {
                try {
                    passwordDao.addPassword(password = password)
                    _passwordAddState.emit(AddPasswordState.Success)
                    _clearInputsSharedFlow.emit(ClearFocus.Clear)
                } catch (ex: Exception) {
                    _passwordAddState.emit(AddPasswordState.Failure("Password could not added. ${ex.message}"))
                }
            } else
                _passwordAddState.emit(AddPasswordState.Failure("You must fill required fields."))
        }
    }

    private fun controlPassword(password: Password): Boolean {
        return listOf(
            password.password,
            password.siteName,
            password.userName
        ).all { it.isNotBlank() and it.isNotEmpty() }
    }

    fun generatePassword(): String {
        val data = passwordGenerator.invoke(length = 8)
        viewModelScope.launch {
            password.emit(data)
        }
        return data
    }

}

sealed interface ClearFocus {
    data object Init : ClearFocus
    data object Clear : ClearFocus
}

sealed class AddPasswordState {
    data object Success : AddPasswordState()
    class Failure(val message: String) : AddPasswordState()
    data object Empty : AddPasswordState()
}

interface AppDispatchers {

    @Suppress("PropertyName")
    val Default: CoroutineContext

    @Suppress("PropertyName")
    val Main: CoroutineContext

    @Suppress("PropertyName")
    val IO: CoroutineContext

}

class DefaultAppDispatchers : AppDispatchers {
    override val Default: CoroutineContext
        get() = Dispatchers.Default
    override val Main: CoroutineContext
        get() = Dispatchers.Main
    override val IO: CoroutineContext
        get() = Dispatchers.IO

}
