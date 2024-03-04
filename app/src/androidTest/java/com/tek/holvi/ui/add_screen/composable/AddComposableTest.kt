package com.tek.holvi.ui.add_screen.composable


import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tek.holvi.theme.HolviTheme
import com.tek.holvi.ui.all_screen.AllScreen
import com.tek.holvi.ui.menuActivity.MenuActivity
import com.tek.holvi.utils.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    private fun openSheet() {
        composeRule.onNodeWithTag("addFab").performClick()
    }


    @Test
    fun add_sheet_displayed() {
        openSheet()
        composeRule.onNodeWithTag("addSheet").assertIsDisplayed()
    }

    @Test
    fun savePassword_success() {
        openSheet()
        with(composeRule) {
            onNodeWithTag("Site Name").performTextInput("Test Site")
            onNodeWithTag("User Name").performTextInput("Test User")
            onNodeWithTag("generateIcon").performClick()
            onNodeWithText("Add").performClick()
            onNodeWithText("Password inserted successfully!").assertIsDisplayed()
        }
    }


    @Test
    fun savePassword_fail_dueToSiteName() {
        openSheet()
        with(composeRule) {
            onNodeWithTag("User Name").performTextInput("Test User")
            onNodeWithTag("generateIcon").performClick()
            onNodeWithText("Add").performClick()
            onNodeWithText("You must fill required fields.").assertIsDisplayed()
        }
    }

    @Test
    fun savePassword_fail_dueToUserName() {
        openSheet()
        with(composeRule) {
            onNodeWithTag("Site Name").performTextInput("Test Site")
            onNodeWithTag("generateIcon").performClick()
            onNodeWithText("Add").performClick()
            onNodeWithText("You must fill required fields.").assertIsDisplayed()
        }
    }


    @Test
    fun savePassword_fail_dueToPassword() {
        openSheet()
        with(composeRule) {
            onNodeWithTag("Site Name").performTextInput("Test Site")
            onNodeWithTag("User Name").performTextInput("Test User")
            onNodeWithText("Add").performClick()
            onNodeWithText("You must fill required fields.").assertIsDisplayed()
        }
    }


}