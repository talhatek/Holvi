package com.tek.holvi.di

import com.tek.holvi.ui.authenticationActivity.AuthenticationViewModel
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.AllViewModel
import com.tek.password.presentation.GenerateViewModel
import com.tek.password.presentation.PortViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModuleTest = module {
    viewModelOf(::AddViewModel)
    viewModelOf(::AllViewModel)
    viewModelOf(::GenerateViewModel)
    viewModelOf(::PortViewModel)
    viewModelOf(::AuthenticationViewModel)
}