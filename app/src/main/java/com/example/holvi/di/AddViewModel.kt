package com.example.holvi.di

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddViewModel : ViewModel() {
    val text = mutableStateOf("")

}