package com.example.holvi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password

@Database(entities = [Password::class], version = 1, exportSchema = false)
abstract class HolviDb : RoomDatabase() {
    abstract val passwordDao: PasswordDao
}