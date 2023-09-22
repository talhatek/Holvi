package com.example.holvi.ui.menu_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.all_screen.composable.AllScreen
import com.example.holvi.ui.generateActivity.composable.GenerateScreen
import com.example.holvi.ui.port_screen.PortScreen
import com.example.holvi.utils.Screen

class MenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.MenuScreen.route,
                    builder = {
                        composable(Screen.MenuScreen.route) {
                            MenuScreen(navController = navController) {
                                this@MenuActivity.finish()
                            }
                        }
                        composable(Screen.AllScreen.route) {
                            AllScreen(navController = navController)
                        }
                        composable(Screen.GenerateScreen.route) {
                            GenerateScreen(navController = navController)
                        }
                        composable(Screen.PortScreen.route) {
                            PortScreen(navController = navController)
                        }
                    }
                )
            }
        }
    }
}

