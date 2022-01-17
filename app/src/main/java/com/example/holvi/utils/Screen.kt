package com.example.holvi.utils

sealed class Screen(val route: String) {
    object MenuScreen : Screen("menu")
    object AddScreen : Screen("add")
    object DeleteScreen : Screen("delete")
    object GenerateScreen : Screen("generate")
    object AllScreen : Screen("all")
}