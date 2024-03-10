package com.tek.database.di

import android.app.Application
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.tek.database.HolviDb
import com.tek.util.Constant
import com.tek.util.Constant.DATA_STORE_REGISTRATION_DEFAULT_KEY
import com.tek.util.Constant.getRoomDbName
import com.tek.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDatabaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), HolviDb::class.java, getRoomDbName())
            .openHelperFactory(get()).build()
    }
    single {
        get<HolviDb>().passwordDao
    }

    single<SupportSQLiteOpenHelper.Factory> {
        Log.e("db issue", "fetch " + getSq(androidApplication()))
        return@single SupportFactory(SQLiteDatabase.getBytes(getSq(androidApplication()).toCharArray()))
    }

    single {
        getDataStore(androidApplication())
    }
}

fun getSq(androidApplication: Application): String {
    return runBlocking {
        androidApplication.dataStore.data.first()[stringPreferencesKey(Constant.DATA_STORE_REGISTRATION_KEY)]
            ?: DATA_STORE_REGISTRATION_DEFAULT_KEY
    }
}

fun getDataStore(androidApplication: Application): DataStore<Preferences> {
    return androidApplication.dataStore
}

