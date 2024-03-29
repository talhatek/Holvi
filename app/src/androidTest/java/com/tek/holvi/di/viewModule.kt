package com.tek.holvi.di

import com.tek.holvi.presentation.AuthenticationViewModel
import com.tek.password.presentation.CrudPasswordViewModel
import com.tek.password.presentation.GenerateViewModel
import com.tek.password.presentation.PortViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModuleTest = module {
    viewModelOf(::CrudPasswordViewModel)
    viewModelOf(::GenerateViewModel)
    viewModelOf(::PortViewModel)
    viewModelOf(::AuthenticationViewModel)
}