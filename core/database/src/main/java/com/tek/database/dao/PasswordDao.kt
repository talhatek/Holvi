package com.tek.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tek.database.data.PasswordDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passworddto")
    fun observeAllPasswords(): Flow<List<PasswordDto>>

    @Query("SELECT * FROM passworddto")
    fun getAllPasswords(): List<PasswordDto>

    @Query("SELECT * FROM passworddto where passworddto.site_name = :siteName")
    suspend fun getPasswordBySiteName(siteName: String): PasswordDto

    @Query(
        " SELECT * FROM passworddto where passworddto.site_name  LIKE :query " +
                " OR   passworddto.user_name  LIKE :query "
    )
    suspend fun searchThroughPasswords(query: String): List<PasswordDto>

    @Query("SELECT passworddto.site_name FROM passworddto")
    fun getAllSiteNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPassword(password: PasswordDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePassword(password: PasswordDto)

    @Query("DELETE FROM passworddto WHERE site_name = :siteName and user_name = :userName")
    suspend fun deletePassword(siteName: String, userName: String): Int

}