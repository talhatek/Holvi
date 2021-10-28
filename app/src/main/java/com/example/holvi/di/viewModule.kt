package com.example.holvi.di

import com.example.holvi.ui.addActivity.AddViewModel
import com.example.holvi.ui.allActivity.AllViewModel
import com.example.holvi.ui.deleteActivity.DeleteViewModel
import org.koin.dsl.module

val viewModule = module {
    single { AddViewModel(get()) }
    single { AllViewModel(get()) }
    single { DeleteViewModel(get()) }
}