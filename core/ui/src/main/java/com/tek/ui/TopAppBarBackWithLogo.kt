package com.tek.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun TopAppBarBackWithLogo(navController: NavController) {
    CenterTopAppBar(
        title = {
            Text(
                text = "Holvi",
                color = HolviTheme.colors.primaryForeground,
                style = HolviTheme.typography.title
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = HolviTheme.colors.primaryForeground,
                )
            }
        }
    )
}

