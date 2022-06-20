package com.example.holvi.ui.all_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AllViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    private val _allPasswords = MutableStateFlow<PasswordsState>(PasswordsState.Init)
    val allPasswords = _allPasswords.asStateFlow()

    val searchQuery = MutableStateFlow("")
    init {
        getAll()
        viewModelScope.launch {
            searchQuery.debounce(444L).filter {
                    query ->
                if (query.isEmpty() || query.length < 2) {
                    return@filter false
                }
                return@filter true
            }.distinctUntilChanged().collectLatest {
                try {
                    val data = passwordDao.searchThroughPasswords("%$it%")
                    _allPasswords.emit(PasswordsState.Success(data = data))
                } catch (ex: Exception) {
                    _allPasswords.emit(PasswordsState.Error(message = ex.message ?: "Unknown Error"))
                }
            }
        }

    }

     fun getAll() {
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

    private fun search(){

    }
}

sealed class PasswordsState {
    object Loading : PasswordsState()
    object Init : PasswordsState()
    class Success(val data: List<Password>) : PasswordsState()
    class Error(val message: String) : PasswordsState()
}