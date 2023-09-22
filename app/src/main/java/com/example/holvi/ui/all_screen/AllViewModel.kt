package com.example.holvi.ui.all_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
class AllViewModel(private val passwordDao: PasswordDao) : ViewModel() {

    private val _allPasswords = MutableStateFlow<PasswordsState>(PasswordsState.Init)
    val allPasswords = _allPasswords.asStateFlow()
    private val _passwordDeleteState = MutableSharedFlow<DeletePasswordState>()

    val passwordDeleteState = _passwordDeleteState.asSharedFlow()
    private val deletedItem = MutableStateFlow<Password?>(null)
    private var clearDeletedItemJob: Job? = null

    val searchQuery = MutableStateFlow("")

    init {
        getAll()
        viewModelScope.launch {
            deletedItem.collectLatest {
                if (it == null) clearDeletedItemJob = null
            }
        }
        viewModelScope.launch {
            searchQuery.debounce(444L).filter { query ->
                if (query.isEmpty() || query.length < 2) {
                    return@filter false
                }
                return@filter true
            }.distinctUntilChanged().collectLatest {
                try {
                    val data = passwordDao.searchThroughPasswords("%$it%")
                    _allPasswords.emit(PasswordsState.Success(data = data))
                } catch (ex: Exception) {
                    _allPasswords.emit(
                        PasswordsState.Error(
                            message = ex.message ?: "Unknown Error"
                        )
                    )
                }
            }
        }

    }

    fun getAll() {
        viewModelScope.launch {
            _allPasswords.emit(PasswordsState.Loading)
            try {
                passwordDao.observeAllPasswords().collectLatest { data ->
                    if (data.isEmpty()) {
                        _allPasswords.emit(PasswordsState.Empty)
                    } else {
                        _allPasswords.emit(PasswordsState.Success(data = data))
                    }
                }
            } catch (ex: Exception) {
                _allPasswords.emit(PasswordsState.Error(message = ex.message ?: "Unknown Error"))
            }
        }
    }


    fun delete(siteName: String) {
        viewModelScope.launch {
            try {
                val item = passwordDao.getPassword(siteName)
                val effectedRowCount = passwordDao.deletePassword(password = item)
                if (effectedRowCount > 0) {
                    deletedItem.value = item
                    clearDeletedItemJob = viewModelScope.launch {
                        delay(4.seconds)
                        if (isActive) {
                            deletedItem.value = null
                        }
                    }
                    _passwordDeleteState.emit(DeletePasswordState.Success)
                } else
                    _passwordDeleteState.emit(DeletePasswordState.NotFound)

            } catch (ex: Exception) {
                _passwordDeleteState.emit(DeletePasswordState.Failure)
            }
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            deletedItem.value?.let {
                passwordDao.addPassword(it)
                deletedItem.value = null
                _passwordDeleteState.emit(DeletePasswordState.Undo)
            } ?: run {
                _passwordDeleteState.emit(DeletePasswordState.Failure)
            }
        }
    }
}

sealed class PasswordsState {
    data object Loading : PasswordsState()
    data object Init : PasswordsState()
    data object Empty : PasswordsState()
    class Success(val data: List<Password>) : PasswordsState()
    class Error(val message: String) : PasswordsState()
}

sealed interface DeletePasswordState {
    data object Success : DeletePasswordState
    data object Undo : DeletePasswordState
    data object NotFound : DeletePasswordState
    data object Failure : DeletePasswordState

}