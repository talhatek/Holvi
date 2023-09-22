package com.example.holvi.utils

sealed class Screen(val route: String) {
    data object MenuScreen : Screen("menu")
    data object AllScreen : Screen("all")
    data object GenerateScreen : Screen("generate")
    data object PortScreen : Screen("port")
}