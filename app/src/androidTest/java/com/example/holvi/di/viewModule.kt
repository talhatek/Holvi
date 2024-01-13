package com.example.holvi.di

import com.example.holvi.ui.add_screen.AddViewModel
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.authenticationActivity.AuthenticationViewModel

import com.example.holvi.ui.generate_screen.GenerateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModuleTest = module {
    viewModel { AddViewModel(get()) }
    viewModel { AllViewModel(get()) }
    viewModel { GenerateViewModel() }
    viewModel { AuthenticationViewModel(get()) }
}