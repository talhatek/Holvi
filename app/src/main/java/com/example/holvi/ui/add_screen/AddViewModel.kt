package com.example.holvi.ui.add_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    val passwordUiHint = mutableStateOf("")
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()
    fun addPassword(password: Password) {
        viewModelScope.launch {
            try {
                passwordDao.addPassword(password = password)
                _passwordAddState.emit(AddPasswordState.Success)
            } catch (ex: Exception) {
                _passwordAddState.emit(AddPasswordState.Failure)

            }
        }
    }

}

sealed class AddPasswordState {
    object Success : AddPasswordState()
    object Failure : AddPasswordState()
    object Empty : AddPasswordState()
}