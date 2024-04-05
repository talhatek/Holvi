package com.tek.database.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Card(
    val id: Int,
    val number: String,
    val exp: String,
    val cvv: String,
    val holder: String,
    val provider: String,
    val company: String,
    val cardColor: Color,
    val textColor: Color
)
