package com.tek.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tek.database.dao.PasswordDao
import com.tek.database.data.Password

@Database(entities = [Password::class], version = 1, exportSchema = false)
abstract class HolviDb : RoomDatabase() {
    abstract val passwordDao: PasswordDao
}