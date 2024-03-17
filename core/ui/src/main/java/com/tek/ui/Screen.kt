package com.tek.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

sealed class Screen(val route: String) {
    data object MenuScreen : Screen("menu")
    data object CardScreen : Screen("card")
    data object PasswordScreen : Screen("password")
    data object AllScreen : Screen("all")
    data object AddScreen : Screen("add")
    data object UpdateScreen : Screen("update")
    data object GenerateScreen : Screen("generate")
    data object PortScreen : Screen("port")
}

fun Screen.replaceWith(param: String, value: String) =
    this.route.replace(oldValue = "{$param}", newValue = value)

fun NavController.navigateWithArgs(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    graph.findNode(route = route)?.id?.let {
        navigate(it, args, navOptions, navigatorExtras)
    }
}