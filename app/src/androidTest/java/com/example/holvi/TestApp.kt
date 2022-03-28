package com.example.holvi

import android.app.Application
import com.example.holvi.di.roomModuleTest
import com.example.holvi.di.viewModuleTest
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
            modules(modules = listOf(roomModuleTest, viewModuleTest))

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}