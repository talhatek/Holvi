package com.example.holvi.di

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.holvi.db.HolviDb
import com.example.holvi.ui.HolviApp
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), HolviDb::class.java, "holvi.db")
            .openHelperFactory(get()).build()
    }
    single {
        get<HolviDb>().passwordDao
    }

    single<SupportSQLiteOpenHelper.Factory> {
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes((androidApplication() as HolviApp).firebaseIdentifier.toCharArray())
        return@single SupportFactory(passphrase)
    }

}