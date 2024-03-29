package com.tek.holvi.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tek.holvi.R
import com.tek.ui.HolviScaffold
import com.tek.ui.MenuScreenContent
import com.tek.ui.Screen
import com.tek.ui.TopAppBarOnlyIcon

@Composable
fun MenuScreen(navController: NavController, onExitClick: () -> Unit) {
    HolviScaffold(
        topBar = {
            TopAppBarOnlyIcon(res = R.drawable.ic_power) {
                onExitClick.invoke()
            }
        },
        content = {
            MenuScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                itemList = listOf(
                    Screen.CardScreen,
                    Screen.PasswordMenuScreen,
                )
            ) { menuType ->
                navController.navigate(menuType)

            }
        }
    )
}

