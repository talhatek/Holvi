package com.tek.holvi.di

import com.tek.holvi.domain.ControlRegistrationUseCase
import com.tek.holvi.domain.RegisterPhoneUseCase
import com.tek.holvi.domain.StoreRegistrationKeyUseCase
import com.tek.holvi.presentation.AuthenticationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authenticationModule = module {
    viewModelOf(::AuthenticationViewModel)
    factoryOf(::ControlRegistrationUseCase)
    factoryOf(::RegisterPhoneUseCase)
    factoryOf(::StoreRegistrationKeyUseCase)

}

