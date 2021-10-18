package com.example.holvi.ui

import android.app.Application
import com.example.holvi.di.viewModule
import org.koin.core.context.startKoin

class HolviApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(viewModule)
        }
    }
}