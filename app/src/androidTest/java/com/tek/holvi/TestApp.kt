package com.tek.holvi

import android.app.Application
import com.tek.database.di.fireStoreModule
import com.tek.database.di.testLocalDatabaseModule
import com.tek.holvi.di.authenticationModule
import com.tek.holvi.di.viewModuleTest
import com.tek.password.di.passwordModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.NONE)
            androidContext(this@TestApp)
            modules(
                modules = listOf(
                    testLocalDatabaseModule,
                    viewModuleTest,
                    authenticationModule,
                    passwordModule,
                    fireStoreModule
                )
            )

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}