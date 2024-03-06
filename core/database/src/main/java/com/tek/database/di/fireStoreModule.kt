package com.tek.database.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tek.database.domain.ExportPasswordUseCase
import com.tek.database.domain.ImportPasswordUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val fireStoreModule = module {
    single { Firebase.firestore }
    factoryOf(::ImportPasswordUseCase)
    factoryOf(::ExportPasswordUseCase)
}