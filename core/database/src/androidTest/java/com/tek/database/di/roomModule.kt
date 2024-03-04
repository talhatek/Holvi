package com.tek.database.di

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.tek.database.HolviDb
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