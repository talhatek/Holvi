package com.tek.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tek.database.data.CardDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM cards")
    fun observeAllCards(): Flow<List<CardDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: CardDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCard(card: CardDto)

    @Query("DELETE FROM cards WHERE card_number = :cardNumber")
    suspend fun deleteCard(cardNumber: String): Int
}