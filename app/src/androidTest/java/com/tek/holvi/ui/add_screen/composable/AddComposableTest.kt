package com.tek.holvi.ui.add_screen.composable


import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tek.holvi.ui.activity.MenuActivity
import com.tek.password.ui.AllScreen
import com.tek.ui.HolviTheme
import com.tek.ui.Screen
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

    @Test
    fun givenAddButtonClicked_shouldDisplayAddSheet() {
        openSheet()
        composeRule.onNodeWithTag("addSheet").assertIsDisplayed()
    }

    @Test
    fun givenValidPasswordDetails_whenSaveButtonClicked_shouldShowSuccessMessage() {
        openSheet()
        with(composeRule) {
            enterPasswordDetails("Test Site", "Test User")
            onNodeWithTag("generateIcon").performClick()
            clickAddButton()
        }
        composeRule.onNodeWithText("Password inserted successfully!").assertIsDisplayed()
    }

    @Test
    fun givenEmptySiteName_whenSaveButtonClicked_shouldShowErrorMessage() {
        openSheet()
        with(composeRule) {
            enterPasswordDetails("", "Test User")
            clickAddButton()
        }
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()
    }

    @Test
    fun givenEmptyUserName_whenSaveButtonClicked_shouldShowErrorMessage() {
        openSheet()
        with(composeRule) {
            enterPasswordDetails("Test Site", "")
            clickAddButton()
        }
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()
    }

    @Test
    fun givenEmptyPassword_whenSaveButtonClicked_shouldShowErrorMessage() {
        openSheet()
        with(composeRule) {
            enterPasswordDetails("Test Site", "Test User")
            clickAddButton()
        }
        composeRule.onNodeWithText("You must fill required fields.").assertIsDisplayed()
    }

    private fun openSheet() {
        composeRule.onNodeWithTag("addFab").performClick()
    }

    private fun ComposeTestRule.enterPasswordDetails(siteName: String, userName: String) {
        onNodeWithTag("Site Name").performTextInput(siteName)
        onNodeWithTag("User Name").performTextInput(userName)
    }

    private fun ComposeTestRule.clickAddButton() {
        onNodeWithText("Add").performClick()
    }
}
