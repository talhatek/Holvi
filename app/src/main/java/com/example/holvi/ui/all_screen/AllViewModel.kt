package com.example.holvi.ui.all_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    private val _allPasswords = MutableStateFlow<PasswordsState>(PasswordsState.Init)
    val allPasswords = _allPasswords.asStateFlow()

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            _allPasswords.emit(PasswordsState.Loading)
            try {
                val data = passwordDao.getAllPasswords()
                _allPasswords.emit(PasswordsState.Success(data = data))
            } catch (ex: Exception) {
                _allPasswords.emit(PasswordsState.Error(message = ex.message ?: "Unknown Error"))
            }
        }
    }
}

sealed class PasswordsState {
    object Loading : PasswordsState()
    object Init : PasswordsState()
    class Success(val data: List<Password>) : PasswordsState()
    class Error(val message: String) : PasswordsState()
}