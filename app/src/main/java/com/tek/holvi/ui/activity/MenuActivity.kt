package com.tek.holvi.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tek.card.ui.AddCardScreen
import com.tek.database.model.Password
import com.tek.holvi.ui.MenuScreen
import com.tek.password.ui.AddPasswordScreen
import com.tek.password.ui.GenerateScreen
import com.tek.password.ui.PasswordMenuScreen
import com.tek.password.ui.PasswordScreen
import com.tek.password.ui.PortScreen
import com.tek.password.ui.UpdateScreen
import com.tek.ui.HolviTheme
import com.tek.ui.Screen
import kotlinx.serialization.json.Json

class MenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
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
                        composable(Screen.PasswordMenuScreen.route) {
                            PasswordMenuScreen(navController = navController)
                        }
                        composable(Screen.CardScreen.route) {
                            AddCardScreen(navController = navController)
                        }
                        composable(
                            Screen.PasswordScreen.route,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Screen.AddPasswordScreen.route, Screen.UpdateScreen.route -> slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Down,
                                        animationSpec = tween(700)
                                    )

                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Screen.AddPasswordScreen.route, Screen.UpdateScreen.route -> slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Up,
                                        animationSpec = tween(700)
                                    )

                                    else -> null
                                }

                            }) {
                            PasswordScreen(navController = navController)
                        }
                        composable(
                            Screen.AddPasswordScreen.route,
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = tween(700)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(700)
                                )
                            }) {
                            AddPasswordScreen(navController = navController)
                        }
                        composable(
                            route = Screen.UpdateScreen.route,
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = tween(700)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(700)
                                )
                            },

                            ) {
                            val password = it.arguments?.getString("password")
                                ?.let { item -> Json.decodeFromString<Password>(item) }
                                ?: throw Exception("Password can not be null!!!")
                            UpdateScreen(navController = navController, item = password)

                        }
                        composable(route = Screen.GenerateScreen.route) {
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

