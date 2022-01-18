package com.example.holvi.ui.add_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import com.example.holvi.utils.PasswordManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    private val passwordState = Channel<Int>()
    val receiver = passwordState.receiveAsFlow()
    private val password = MutableStateFlow("")
    val passwordStateFlow = password.asStateFlow()
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()
    private val passwordManager = PasswordManager()
    fun addPassword(password: Password) {
        viewModelScope.launch {
            val controlPassword = controlPassword(password = password)
            if (controlPassword) {
                try {
                    passwordDao.addPassword(password = password)
                    _passwordAddState.emit(AddPasswordState.Success)
                    passwordState.send(1)
                } catch (ex: Exception) {
                    _passwordAddState.emit(AddPasswordState.Failure("Password could not added."))
                }
            } else
                _passwordAddState.emit(AddPasswordState.Failure("You must fill required fields."))
        }
    }

    private fun controlPassword(password: Password): Boolean {
        return !(password.password.isBlank() || password.siteName.isBlank() || password.userName.isBlank())
    }

    fun generatePassword(): String {
        val data = passwordManager.generatePassword(length = 8)
        viewModelScope.launch {
            password.value = data

        }
        return data
    }
}

sealed class AddPasswordState {
    object Success : AddPasswordState()
    class Failure(val message: String) : AddPasswordState()
    object Empty : AddPasswordState()
}