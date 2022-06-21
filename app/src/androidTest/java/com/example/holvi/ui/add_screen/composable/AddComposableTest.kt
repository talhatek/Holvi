package com.example.holvi.ui.add_screen.composable


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holvi.currentText
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.menu_screen.MenuActivity
import com.example.holvi.utils.Screen
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test



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
        assertThat(Integer(8)).isEqualTo(text.toString().length)
    }

    @Test
    fun savePassword_success() {

        composeRule.onNodeWithText("Site Name").performTextInput("Test Site")
        composeRule.onNodeWithText("User Name").performTextInput("Test User")
        composeRule.onNodeWithTag("G").performClick()
        composeRule.onNodeWithText("Add").performClick()
        Thread.sleep(1_000L)
        composeRule.onNodeWithText("Password added successfully.").assertIsDisplayed()

    }


    @Test
    fun savePassword_fail_dueToSiteName() {

        composeRule.onNodeWithText("User Name").performTextInput("Test User")
        composeRule.onNodeWithTag("G").performClick()
        composeRule.onNodeWithText("Add").performClick()
        Thread.sleep(1_000L)
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()

    }

    @Test
    fun savePassword_fail_dueToUserName() {
        composeRule.onNodeWithText("Site Name").performTextInput("Test Site")
        composeRule.onNodeWithTag("G").performClick()
        composeRule.onNodeWithText("Add").performClick()
        Thread.sleep(1_000L)
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()
    }


    @Test
    fun savePassword_fail_dueToPassword() {
        composeRule.onNodeWithText("Site Name").performTextInput("Test Site")
        composeRule.onNodeWithText("User Name").performTextInput("Test User")
        composeRule.onNodeWithText("Add").performClick()
        Thread.sleep(1000L)
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()
    }


}