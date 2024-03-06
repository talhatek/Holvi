package com.tek.holvi.ui

import android.app.Application
import com.tek.database.di.fireStoreModule
import com.tek.database.di.localDatabaseModule

import com.tek.holvi.BuildConfig
import com.tek.holvi.di.authenticationModule
import com.tek.password.di.passwordModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class HolviApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@HolviApp)
            modules(
                listOf(
                    authenticationModule,
                    localDatabaseModule,
                    passwordModule,
                    fireStoreModule
                )
            )
        }
    }
}