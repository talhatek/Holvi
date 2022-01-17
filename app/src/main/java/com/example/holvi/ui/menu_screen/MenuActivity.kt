package com.example.holvi.ui.menu_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holvi.ui.add_screen.composable.AddScreen
import com.example.holvi.ui.all_screen.composable.AllScreen
import com.example.holvi.ui.delete_screen.composable.DeleteScreen
import com.example.holvi.ui.generateActivity.composable.GenerateScreen
import com.example.holvi.utils.Screen
import kotlin.system.exitProcess

@ExperimentalComposeUiApi
class MenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                    composable(Screen.AddScreen.route) {
                        AddScreen(navController = navController)
                    }
                    composable(Screen.AllScreen.route) {
                        AllScreen(navController = navController)
                    }
                    composable(Screen.DeleteScreen.route) {
                        DeleteScreen(navController = navController)
                    }
                    composable(Screen.GenerateScreen.route) {
                        GenerateScreen(navController = navController)
                    }
                })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }
}

