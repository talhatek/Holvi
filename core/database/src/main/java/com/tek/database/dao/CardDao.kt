package com.tek.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tek.database.data.CardDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM carddto")
    fun observeAllCards(): Flow<List<CardDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: CardDto)

}