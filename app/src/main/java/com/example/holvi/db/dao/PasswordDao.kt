package com.example.holvi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    suspend fun getAllPasswords(): List<Password>

    @Query("SELECT password.site_name FROM password")
    fun getAllSiteNames(): Flow<List<String>>

    @Insert
    suspend fun addPassword(password: Password)

    @Query("DELETE FROM  password WHERE site_name = :siteName")
    suspend fun deletePassword(siteName: String): Int


}