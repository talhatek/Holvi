package com.tek.password.di

import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.CrudViewModel
import com.tek.password.presentation.GenerateViewModel
import com.tek.password.presentation.PortViewModel
import com.tek.util.AppDispatchers
import com.tek.util.HolviAppDispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.Koin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val passwordModule = module {

    viewModelOf(::CrudViewModel)
    viewModelOf(::GenerateViewModel)
    viewModelOf(::PortViewModel)
    factoryOf(::PasswordGeneratorUseCase)
    factoryOf<AppDispatchers> { HolviAppDispatchers() }

}

fun Koin.getViewModelScope(name: String): Scope {
    val scope = getOrCreateScope(name, named(name))
    if (scope.closed) {
        deleteScope(name)
        return getOrCreateScope(name, named(name))
    }
    return scope
}