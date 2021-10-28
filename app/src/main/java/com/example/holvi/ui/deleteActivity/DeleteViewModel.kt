package com.example.holvi.ui.deleteActivity

import androidx.lifecycle.ViewModel
import com.example.holvi.db.dao.PasswordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DeleteViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    fun getAllSiteNames(): Flow<List<String>> = passwordDao.getAllSiteNames()
    private val _passwordDeleteState = MutableSharedFlow<DeletePasswordState>()
    val passwordDeleteState = _passwordDeleteState.asSharedFlow()
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

    sealed class DeletePasswordState {
        object Success : DeletePasswordState()
        object Failure : DeletePasswordState()
        object Empty : DeletePasswordState()
        object SuccessEmpty : DeletePasswordState()
    }
}