package com.tek.holvi.ui.generate_screen


import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.holvi.utils.PasswordManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GenerateViewModel : ViewModel() {
    val symbolState = mutableStateOf(true)
    val numberState = mutableStateOf(true)
    val upperCaseState = mutableStateOf(true)
    val lowerCaseState = mutableStateOf(true)
    val dropdownItems = mutableListOf<Int>()
    private val _activeCount = MutableStateFlow(4)
    val activeCount = _activeCount.asStateFlow()
    val currentPassword = mutableStateOf("")
    private val passwordManager = PasswordManager()
    val currentSelectedLength = mutableStateOf(0)
    val forbiddenLetters = mutableStateOf("")
    val lengthSelectorText = mutableStateOf("Password length")
    private val _uiEvent = MutableSharedFlow<GenerateViewUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            _activeCount.collectLatest {
                val tmp = mutableListOf<Int>()
                for (i in 1..50) {
                    if (i % _activeCount.value == 0 && !dropdownItems.contains(i) && i > it)
                        tmp.add(i)
                }
                dropdownItems.clear()
                dropdownItems.addAll(tmp)
            }
        }
    }


    fun updateActiveCount(isActive: Boolean) {
        _activeCount.value = if (isActive) _activeCount.value + 1 else _activeCount.value - 1
        lengthSelectorText.value = "Password length"
        currentSelectedLength.value = -1

    }

    fun generatePassword() {
        if (isProducible()) {
            currentPassword.value = passwordManager.generatePassword(
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

    sealed class GenerateViewUiEvent {
        class SnackbarEvent(val message: String) : GenerateViewUiEvent()
    }

    private fun sendUiEvent(event: GenerateViewUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    companion object {
        const val SCOPE_NAME = "GENERATE_VIEW_MODEL_SCOPE"
    }
}