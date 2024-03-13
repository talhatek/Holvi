package com.tek.ui

sealed class Screen(val route: String) {
    data object MenuScreen : Screen("menu")
    data object CardScreen : Screen("card")
    data object PasswordScreen : Screen("password")
    data object AllScreen : Screen("all")
    data object AddScreen : Screen("add")
    data object GenerateScreen : Screen("generate")
    data object PortScreen : Screen("port")
}