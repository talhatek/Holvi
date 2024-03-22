package com.tek.password.presentation


import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GenerateViewModel(
    private val passwordGenerator: PasswordGeneratorUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {
    val symbolState = MutableStateFlow(true)
    val numberState = MutableStateFlow(true)
    val upperCaseState = MutableStateFlow(true)
    val lowerCaseState = MutableStateFlow(true)
    val dropdownItems = MutableStateFlow<List<Int>>(emptyList())
    private val _activeCount = MutableStateFlow(4)
    val activeCount = _activeCount.asStateFlow()
    val currentPassword = MutableStateFlow("")

    val currentSelectedLength = MutableStateFlow(0)
    val forbiddenLetters = MutableStateFlow("")
    val lengthSelectorText = MutableStateFlow("Password length")
    private val _uiEvent = MutableSharedFlow<GenerateViewUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch(appDispatchers.Default) {
            _activeCount.collectLatest {
                dropdownItems.value = generateSequence(it) { value ->
                    if (value.plus(it) < LENGTH_LIMIT) {
                        value.plus(it)
                    } else {
                        null
                    }
                }.toList()
            }
        }
    }


    fun updateActiveCount(isActive: Boolean) {
        _activeCount.value =
            (if (isActive) _activeCount.value + 1 else _activeCount.value - 1).coerceIn(
                minimumValue = 1,
                maximumValue = 4
            )
        lengthSelectorText.value = "Password length"
        currentSelectedLength.value = -1

    }

    fun generatePassword() {
        if (isProducible()) {
            currentPassword.value = passwordGenerator.invoke(
                lowerCaseState.value,
                upperCaseState.value,
                numberState.value,
                symbolState.value,
                currentSelectedLength.value,
                forbiddenLetters.value.toCharArray()
            )
        } else {
            sendUiEvent(GenerateViewUiEvent.SnackbarEvent("You must fill required fields."))
        }

    }

    private fun isProducible() = currentSelectedLength.value > 0


    fun copyToClipBoard(clipboardManager: ClipboardManager) {
        if (currentPassword.value.isEmpty())
            sendUiEvent(GenerateViewUiEvent.SnackbarEvent("You must generate password!"))
        else {
            val clipData =
                ClipData.newPlainText("label", currentPassword.value)
            clipboardManager.setPrimaryClip(clipData)
            sendUiEvent(GenerateViewUiEvent.SnackbarEvent("Copied to clipboard!"))
        }

    }

    private fun sendUiEvent(event: GenerateViewUiEvent) {
        viewModelScope.launch(appDispatchers.Main) {
            _uiEvent.emit(event)
        }
    }

    companion object {
        const val SCOPE_NAME = "GENERATE_VIEW_MODEL_SCOPE"
        const val LENGTH_LIMIT = 50
    }
}

sealed class GenerateViewUiEvent {
    class SnackbarEvent(val message: String) : GenerateViewUiEvent()
}
