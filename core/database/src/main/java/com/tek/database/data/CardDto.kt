package com.tek.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "card_number") val cardNumber: String,
    @ColumnInfo(name = "card_holder") val cardHolder: String,
    @ColumnInfo(name = "expiration") val expiration: String,
    @ColumnInfo(name = "cvv") val cvv: String
)
