package com.tek.holvi.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun HolviTheme(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme.copy(
        primary = PrimaryGreen,
        onBackground = SecondPrimary,
        secondary = SecondPrimaryDark,
        background = BackGround,
    )

    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        typography = Typography,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = PrimaryTextColor),
                content = content
            )
        }
    )
}