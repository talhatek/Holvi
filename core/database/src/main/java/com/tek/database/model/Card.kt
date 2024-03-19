package com.tek.database.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Card(
    val id: Int,
    val number: String,
    val exp: String,
    val cvv: String,
    val holderName: String,
    val color: Color
)
