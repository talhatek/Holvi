package com.tek.holvi.utils

sealed class Screen(val route: String) {
    data object MenuScreen : Screen("menu")
    data object CardScreen : Screen("card")
    data object PasswordScreen : Screen("password")
    data object AllScreen : Screen("all")
    data object GenerateScreen : Screen("generate")
    data object PortScreen : Screen("port")
}