package com.example.holvi.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.holvi.R

// Set of Material typography styles to start with
val PoppinsBold = FontFamily(Font(R.font.poppins_bold))
val PoppinsSemiBold = FontFamily(Font(R.font.poppins_semi_bold))
val PoppinsMedium = FontFamily(Font(R.font.poppins_medium))
val PoppinsRegular = FontFamily(Font(R.font.poppins_regular))
val PoppinsLight = FontFamily(Font(R.font.poppins_light))
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PoppinsMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = PoppinsSemiBold,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PoppinsMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )


)