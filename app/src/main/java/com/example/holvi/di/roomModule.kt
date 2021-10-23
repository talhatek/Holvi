package com.example.holvi.di

import androidx.room.Room
import com.example.holvi.db.HolviDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), HolviDb::class.java, "holvi_db").build()
    }
    single {
        get<HolviDb>().passwordDao
    }

}