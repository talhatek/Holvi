package com.tek.card.di

import com.tek.card.presentation.CrudCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val cardModule = module {
    viewModelOf(::CrudCardViewModel)
}