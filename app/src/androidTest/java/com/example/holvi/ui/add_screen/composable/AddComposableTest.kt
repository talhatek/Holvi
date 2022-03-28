package com.example.holvi.ui.add_screen.composable


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holvi.currentText
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.menu_screen.MenuActivity
import com.example.holvi.utils.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalComposeUiApi::class)
class AddComposableTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MenuActivity>()

    @Before
    fun setUp() {
        composeRule.setContent {
            val navController = rememberNavController()
            HolviTheme {
                NavHost(navController = navController, startDestination = Screen.AddScreen.route) {
                    composable(Screen.AddScreen.route) {
                        AddScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun generateButton_isWorking() {
        composeRule.onNodeWithTag("G").performClick()
        val text = composeRule.onNodeWithTag("PasswordTextField").currentText()
        assertEquals(8, text.toString().length)
    }
}