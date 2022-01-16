package com.example.holvi.ui.deleteActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DeleteViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    fun getAllSiteNames(): Flow<List<String>> = passwordDao.getAllSiteNames()
    private val _passwordDeleteState = MutableSharedFlow<DeletePasswordState>()
    val passwordDeleteState = _passwordDeleteState.asSharedFlow()

    private val _isEmptyState=MutableSharedFlow<IsEmptyState>()
    val isEmptyState = _isEmptyState.asSharedFlow()
    suspend fun delete(siteName: String) {
        try {
            val effectedRowCount = passwordDao.deletePassword(siteName = siteName)
            if (effectedRowCount > 0)
                _passwordDeleteState.emit(DeletePasswordState.Success)
            else
                _passwordDeleteState.emit(DeletePasswordState.SuccessEmpty)

        } catch (ex: Exception) {
            _passwordDeleteState.emit(DeletePasswordState.Failure)

        }
    }

     fun warnUi(){
        viewModelScope.launch {
            _isEmptyState.emit(IsEmptyState.Empty)

        }

    }

    sealed class DeletePasswordState {
        object Success : DeletePasswordState()
        object Failure : DeletePasswordState()
        object Empty : DeletePasswordState()
        object SuccessEmpty : DeletePasswordState()
    }
    sealed class IsEmptyState{
        object Empty:IsEmptyState()
        object NotEmpty:IsEmptyState()
    }
}