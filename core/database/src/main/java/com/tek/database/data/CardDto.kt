package com.tek.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "card_number") val number: String,
    @ColumnInfo(name = "card_holder") val holder: String,
    @ColumnInfo(name = "card_expiration") val expiration: String,
    @ColumnInfo(name = "card_cvv") val cvv: String,
    @ColumnInfo(name = "card_provider") val provider: String,
    @ColumnInfo(name = "card_company") val company: String,
    @ColumnInfo(name = "card_color") val color: String,
    @ColumnInfo(name = "card_text_color") val textColor: String
)
