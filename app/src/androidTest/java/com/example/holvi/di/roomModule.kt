package com.example.holvi.di

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.holvi.db.HolviDb
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModuleTest = module {
    single {
        Room.inMemoryDatabaseBuilder(androidApplication(), HolviDb::class.java)
            .openHelperFactory(get()).build()
    }
    single {
        get<HolviDb>().passwordDao
    }

    single<SupportSQLiteOpenHelper.Factory> {
        return@single SupportFactory("passphrase".toByteArray())
    }

}