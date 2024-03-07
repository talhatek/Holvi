package com.tek.database.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.tek.database.HolviDb
import com.tek.util.AppDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDatabaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), HolviDb::class.java, "holvi.db")
            .openHelperFactory(get()).build()
    }
    single {
        get<HolviDb>().passwordDao
    }

    single<SupportSQLiteOpenHelper.Factory> {
        val key = getSq(androidApplication(), get())
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes(key.toCharArray())
        return@single SupportFactory(passphrase)
    }

    single {
        getDataStore(androidApplication())
    }

}
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "registration")

fun getSq(androidApplication: Application, appDispatchers: AppDispatchers): String {
    return runBlocking(appDispatchers.IO) {
        androidApplication.dataStore.data.first()[stringPreferencesKey("sq")] ?: "empty"
    }
}

fun getDataStore(androidApplication: Application): DataStore<Preferences> {
    return androidApplication.dataStore

}

