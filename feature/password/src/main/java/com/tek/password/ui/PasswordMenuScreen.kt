package com.tek.password.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tek.ui.HolviScaffold
import com.tek.ui.MenuScreenContent
import com.tek.ui.Screen
import com.tek.ui.TopAppBarBackWithLogo


@Composable
fun PasswordMenuScreen(navController: NavController) {
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController)
        },
        content = {
            MenuScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                itemList = listOf(
                    Screen.AllScreen,
                    Screen.GenerateScreen,
                    Screen.PortScreen,
                )
            ) { menuType ->
                navController.navigate(menuType)
            }
        }
    )
}

