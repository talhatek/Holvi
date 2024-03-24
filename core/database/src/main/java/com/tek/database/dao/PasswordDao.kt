package com.tek.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tek.database.data.PasswordDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query(
        " SELECT * FROM passwords where passwords.site_name  LIKE :query " +
                " OR   passwords.user_name  LIKE :query "
    )
    fun observeAllPasswords(query: String): Flow<List<PasswordDto>>

    @Query(
        " SELECT * FROM passwords where passwords.site_name  LIKE :query " +
                " OR   passwords.user_name  LIKE :query "
    )
    fun pagingSourcePasswords(query: String): PagingSource<Int, PasswordDto>

    @Query("SELECT * FROM passwords")
    fun getAllPasswords(): List<PasswordDto>

    @Query("SELECT * FROM passwords where passwords.id = :id")
    suspend fun getPasswordBySiteName(id: Int): PasswordDto


    @Query("SELECT passwords.site_name FROM passwords")
    fun getAllSiteNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPassword(password: PasswordDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePassword(password: PasswordDto)

    @Query("DELETE FROM passwords WHERE site_name = :siteName and user_name = :userName")
    suspend fun deletePassword(siteName: String, userName: String): Int

}