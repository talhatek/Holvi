package com.example.holvi.ui.generateActivity


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holvi.utils.PasswordManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GenerateViewModel : ViewModel() {
    val symbolState = mutableStateOf(true)
    val numberState = mutableStateOf(true)
    val upperCaseState = mutableStateOf(true)
    val lowerCaseState = mutableStateOf(true)
    val dropdownItems = mutableListOf<Int>()
    private val activeCount = MutableStateFlow(4)
    val currentPassword = mutableStateOf("")
    val passwordManager = PasswordManager()
    val currentSelectedLength = mutableStateOf(0)


    init {
        viewModelScope.launch {
            activeCount.collectLatest {
                val tmp = mutableListOf<Int>()
                for (i in 1..50) {
                    if (i % activeCount.value == 0 && !dropdownItems.contains(i) && i > it)
                        tmp.add(i)
                }
                dropdownItems.clear()
                dropdownItems.addAll(tmp)
            }
        }
    }


    fun updateActiveCount(isActive: Boolean) {
        activeCount.value = if (isActive) activeCount.value + 1 else activeCount.value - 1
    }

    fun generatePassword() {
        currentPassword.value = passwordManager.generatePassword(
            lowerCaseState.value,
            upperCaseState.value,
            numberState.value,
            symbolState.value,
            currentSelectedLength.value
        )
    }

}