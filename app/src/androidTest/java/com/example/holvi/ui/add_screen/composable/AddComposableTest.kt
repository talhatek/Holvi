package com.example.holvi.ui.add_screen.composable


import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.all_screen.AllScreen
import com.example.holvi.ui.menuActivity.MenuActivity
import com.example.holvi.utils.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalComposeUiApi::class)
class AddComposableTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MenuActivity>()


    @Before
    fun setUp() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            HolviTheme {
                NavHost(navController = navController, startDestination = Screen.AllScreen.route) {
                    composable(Screen.AllScreen.route) {
                        AllScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun generateButton_isWorking() {
        composeRule.onNodeWithTag("addFab").performClick()
        Thread.sleep(1_000L)
        composeRule.onNodeWithTag("addSheet").assertIsDisplayed()
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