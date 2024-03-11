package com.tek.ui

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable

@Composable
fun holviButtonColors() = ButtonColors(
    containerColor = HolviTheme.colors.primaryBackground,
    contentColor = HolviTheme.colors.primaryForeground,
    disabledContainerColor = HolviTheme.colors.primaryBackground,
    disabledContentColor = HolviTheme.colors.primaryForeground,
)