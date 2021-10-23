package com.example.holvi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    fun getAllPasswords(): Flow<List<Password>>

    @Insert
    suspend fun addPassword(password: Password)

    @Query("DELETE FROM  password WHERE id = :passwordId")
    suspend fun deletePassword(passwordId: Int)


}