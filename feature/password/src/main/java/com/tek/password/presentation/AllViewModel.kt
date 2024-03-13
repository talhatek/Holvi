package com.tek.password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.ObservePasswordUseCase
import com.tek.database.domain.SearchPasswordUseCase
import com.tek.database.model.Password
import com.tek.util.AppDispatchers
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(FlowPreview::class)
class AllViewModel(
    private val getPasswordBySiteName: GetPasswordBySiteNameUseCase,
    private val addPassword: AddPasswordUseCase,
    private val searchPassword: SearchPasswordUseCase,
    private val deletePassword: DeletePasswordUseCase,
    private val observePassword: ObservePasswordUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val _allPasswords = MutableStateFlow<PasswordsState>(PasswordsState.Init)
    val allPasswords = _allPasswords.asStateFlow()

    private val _passwordDeleteState = MutableSharedFlow<DeletePasswordState>()
    val passwordDeleteState = _passwordDeleteState.asSharedFlow()

    private val deletedItem = MutableStateFlow<Password?>(null)
    private var clearDeletedItemJob: Job? = null

    val searchQuery = MutableStateFlow("")

    init {
        getAll()
        viewModelScope.launch(appDispatchers.IO) {
            deletedItem.collectLatest {
                if (it == null) clearDeletedItemJob = null
            }
        }
        viewModelScope.launch(appDispatchers.IO) {
            searchQuery.debounce(250L).filter { it.isNotBlank() }.distinctUntilChanged()
                .collectLatest {
                    try {
                        val data = searchPassword.invoke(it)
                        sortAndSet(data = data)
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
        viewModelScope.launch(appDispatchers.IO) {
            _allPasswords.emit(PasswordsState.Loading)
            try {
                observePassword().collectLatest { data ->
                    if (data.isEmpty()) {
                        _allPasswords.emit(PasswordsState.Empty)
                    } else {
                        sortAndSet(data)
                    }
                }
            } catch (ex: Exception) {
                _allPasswords.emit(PasswordsState.Error(message = ex.message ?: "Unknown Error"))
            }
        }
    }


    fun delete(siteName: String) {
        viewModelScope.launch(appDispatchers.IO) {
            try {
                val item = getPasswordBySiteName.invoke(siteName)
                val effectedRowCount = deletePassword.invoke(item)
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
        viewModelScope.launch(appDispatchers.IO) {
            deletedItem.value?.let {
                addPassword.invoke(it)
                deletedItem.value = null
                _passwordDeleteState.emit(DeletePasswordState.Undo)
            } ?: run {
                _passwordDeleteState.emit(DeletePasswordState.Failure)
            }
        }
    }

    private fun sortAndSet(data: List<Password>) {
        viewModelScope.launch(appDispatchers.IO) {
            _allPasswords.emit(
                PasswordsState.Success(
                    data = data.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.siteName })
                        .toPersistentList()
                )
            )
        }
    }
}

sealed class PasswordsState {
    data object Loading : PasswordsState()
    data object Init : PasswordsState()
    data object Empty : PasswordsState()
    class Success(val data: PersistentList<Password>) : PasswordsState()
    class Error(val message: String) : PasswordsState()
}

sealed interface DeletePasswordState {
    data object Success : DeletePasswordState
    data object Undo : DeletePasswordState
    data object NotFound : DeletePasswordState
    data object Failure : DeletePasswordState

}