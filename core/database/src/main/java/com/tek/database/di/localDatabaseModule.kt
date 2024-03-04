package com.tek.database.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.tek.database.HolviDb
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
        val key = androidApplication().getSharedPreferences(
            "openHelperPreferences",
            Context.MODE_PRIVATE
        ).getString("sq", "default") ?: "default"
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes(key.toCharArray())
        return@single SupportFactory(passphrase)
    }

    single {
        getSharedPrefs(androidApplication()).edit()
    }

}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("openHelperPreferences", Context.MODE_PRIVATE)
}