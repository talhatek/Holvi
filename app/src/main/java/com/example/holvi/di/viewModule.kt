package com.example.holvi.di

import com.example.holvi.ui.add_screen.AddViewModel
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.delete_screen.DeleteViewModel
import com.example.holvi.ui.generateActivity.GenerateViewModel
import org.koin.dsl.module

val viewModule = module {
    single { AddViewModel(get()) }
    single { AllViewModel(get()) }
    single { DeleteViewModel(get()) }
    single { GenerateViewModel() }
}