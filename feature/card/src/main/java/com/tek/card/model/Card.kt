package com.tek.card.model

import androidx.compose.ui.graphics.Color

data class Card(
    val number: String,
    val exp: String,
    val cvv: String,
    val holderName: String,
    val color: Color
)
