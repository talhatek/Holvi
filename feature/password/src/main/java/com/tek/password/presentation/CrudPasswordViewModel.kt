package com.tek.password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.PagingPasswordUseCase
import com.tek.database.domain.UpdatePasswordUseCase
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(FlowPreview::class)
class CrudPasswordViewModel(
    private val pagingPassword: PagingPasswordUseCase,
    private val getPasswordBySiteName: GetPasswordBySiteNameUseCase,
    private val addPassword: AddPasswordUseCase,
    private val updatePassword: UpdatePasswordUseCase,
    private val passwordGenerator: PasswordGeneratorUseCase,
    private val deletePassword: DeletePasswordUseCase,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    private val _passwordDeleteState = MutableSharedFlow<DeletePasswordState>()
    val passwordDeleteState = _passwordDeleteState.asSharedFlow()
    private val _clearInputsSharedFlow = MutableSharedFlow<ClearFocus>()
    val clearInputsSharedFlow = _clearInputsSharedFlow.asSharedFlow()
    private val password = MutableStateFlow("")
    val passwordStateFlow = password.asStateFlow()
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()
    private val deletedItem = MutableStateFlow<Password?>(null)

    private val queryInput = MutableStateFlow("")

    val queryFlow = queryInput.debounce(250).distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val paging = queryFlow.flatMapLatest {
        pagingPassword.invoke(it)
    }.flowOn(appDispatchers.IO).cachedIn(viewModelScope)

    fun updateQuery(query: String) {
        queryInput.value = query
    }

    fun add(password: Password) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _passwordAddState.emit(AddPasswordState.Failure("${ex.message}"))
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            if (!control(password = password)) {
                throw Exception("You must fill required fields.")
            }
            addPassword.invoke(password)
            _passwordAddState.emit(AddPasswordState.Success)
            _clearInputsSharedFlow.emit(ClearFocus.Clear)

        }
    }

    fun update(password: Password) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _passwordAddState.emit(AddPasswordState.Failure("${ex.message}"))
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            if (!control(password = password)) {
                throw Exception("You must fill required fields.")
            }
            updatePassword.invoke(password)
            _passwordAddState.emit(AddPasswordState.Success)
            _clearInputsSharedFlow.emit(ClearFocus.Clear)

        }
    }

    fun delete(id: Int) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _passwordDeleteState.emit(DeletePasswordState.Failure)
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            val item = getPasswordBySiteName.invoke(id)
            val effectedRowCount = deletePassword.invoke(item)
            if (effectedRowCount > 0) {

                deletedItem.value = item
                viewModelScope.launch(appDispatchers.IO) {
                    delay(4.seconds)
                    if (isActive) {
                        deletedItem.value = null
                    }
                }
                _passwordDeleteState.emit(DeletePasswordState.Success)
            } else {
                _passwordDeleteState.emit(DeletePasswordState.NotFound)

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


    private fun control(password: Password): Boolean {
        return listOf(
            password.password,
            password.siteName,
            password.userName
        ).all { it.isNotBlank() and it.isNotEmpty() }
    }

    fun generate(): String {
        val data = passwordGenerator.invoke(length = 8)
        viewModelScope.launch(appDispatchers.Default) {
            password.emit(data)
        }
        return data
    }

}

sealed interface DeletePasswordState {
    data object Success : DeletePasswordState
    data object Undo : DeletePasswordState
    data object NotFound : DeletePasswordState
    data object Failure : DeletePasswordState

}

sealed interface ClearFocus {
    data object Clear : ClearFocus
}

sealed class AddPasswordState {
    data object Success : AddPasswordState()
    class Failure(val message: String) : AddPasswordState()
}
