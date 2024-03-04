package com.tek.holvi.di


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tek.holvi.ui.add_screen.AddViewModel
import com.tek.holvi.ui.all_screen.AllViewModel
import com.tek.holvi.ui.authenticationActivity.AuthenticationViewModel
import com.tek.holvi.ui.generate_screen.GenerateViewModel
import com.tek.holvi.ui.port_screen.PortViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::AddViewModel)
    viewModelOf(::AllViewModel)
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::PortViewModel)
    viewModelOf(::GenerateViewModel)

}

fun Koin.getViewModelScope(name: String): Scope {
    val scope = getOrCreateScope(name, named(name))
    if (scope.closed) {
        deleteScope(name)
        return getOrCreateScope(name, named(name))
    }
    return scope
}

val firebaseModule = module {
    factory { Firebase.firestore }
}