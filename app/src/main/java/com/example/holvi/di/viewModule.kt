package com.example.holvi.di

import org.koin.dsl.module

val viewModule = module {
    single { AddViewModel() }
}