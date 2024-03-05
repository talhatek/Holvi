package com.tek.holvi.ui.menuActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.tek.holvi.ui.all_screen.AllScreen
import com.tek.holvi.ui.generate_screen.GenerateScreen
import com.tek.holvi.ui.port_screen.PortScreen
import com.tek.holvi.utils.Screen
import com.tek.ui.HolviTheme

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

