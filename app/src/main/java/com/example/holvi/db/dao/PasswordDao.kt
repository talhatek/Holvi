package com.example.holvi.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    fun observeAllPasswords(): Flow<List<Password>>

    @Query("SELECT * FROM password")
    fun getAllPasswords(): List<Password>

    @Query("SELECT * FROM password where password.site_name = :siteName")
    suspend fun getPassword(siteName: String): Password

    @Query(
        " SELECT * FROM password where password.site_name  LIKE :query " +
                " OR   password.user_name  LIKE :query "
    )
    suspend fun searchThroughPasswords(query: String): List<Password>

    @Query("SELECT password.site_name FROM password")
    fun getAllSiteNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPassword(password: Password)

    @Delete
    suspend fun deletePassword(password: Password): Int

}