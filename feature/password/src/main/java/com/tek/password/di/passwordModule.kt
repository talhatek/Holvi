package com.tek.password.di

import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.AllViewModel
import com.tek.password.presentation.GenerateViewModel
import com.tek.password.presentation.PortViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val passwordModule = module {
    viewModelOf(::AddViewModel)
    viewModelOf(::AllViewModel)
    viewModelOf(::GenerateViewModel)
    viewModelOf(::PortViewModel)

}

fun Koin.getViewModelScope(name: String): Scope {
    val scope = getOrCreateScope(name, named(name))
    if (scope.closed) {
        deleteScope(name)
        return getOrCreateScope(name, named(name))
    }
    return scope
}