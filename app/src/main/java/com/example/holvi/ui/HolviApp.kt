package com.example.holvi.ui

import android.app.Application
import com.example.holvi.BuildConfig
import com.example.holvi.di.dbModule
import com.example.holvi.di.roomModule
import com.example.holvi.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class HolviApp : Application() {
    var firebaseIdentifier: String = "default"

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@HolviApp)
            modules(listOf(roomModule, viewModule, dbModule))
        }
    }

    fun initSq(id: String) {
        firebaseIdentifier = id
    }
}