package com.example.holvi.ui.addActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.holvi.db.model.Password
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.addActivity.composable.InputView
import com.example.holvi.ui.addActivity.composable.PasswordInputView
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleTextButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.utils.MenuType.Companion.ADD
import com.example.holvi.utils.PasswordManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

class AddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                val scaffoldState = rememberScaffoldState()
                var siteName by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var userName by remember { mutableStateOf("") }
                val myAddViewModel = get<AddViewModel>()
                val scope = rememberCoroutineScope()
                LaunchedEffect(key1 = true) {
                    myAddViewModel.passwordAddState.collect {
                        when (it) {
                            is AddPasswordState.Success -> {
                                scaffoldState.snackbarHostState.showSnackbar("Password added successfully.")
                            }
                            is AddPasswordState.Failure -> {
                                scaffoldState.snackbarHostState.showSnackbar("Password could not added.")
                            }
                            else -> Unit
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        TopAppBarBackWithLogo {
                            this.finish()
                            myAddViewModel.passwordUiHint.value = ""

                        }
                    },
                    bottomBar = {
                        BottomButton(text = ADD) {
                            scope.launch(Dispatchers.IO) {
                                myAddViewModel.addPassword(
                                    Password(
                                        id = 0,
                                        siteName = siteName,
                                        password = password,
                                        userName = userName
                                    )
                                )
                            }

                        }
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.8f),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val passwordManager = PasswordManager()
                            InputView(hintParam = "Site Name") {
                                siteName = it
                            }

                            InputView(hintParam = "User Name") {
                                userName = it

                            }
                            PasswordInputView(myAddViewModel, hintParam = "Password") {
                                password = it
                            }
                            CircleTextButton(text = "G", percentage = 20) {
                                scope.launch(Dispatchers.Default) {
                                    myAddViewModel.passwordUiHint.value =
                                        passwordManager.generatePassword(length = 8)
                                    password = myAddViewModel.passwordUiHint.value
                                }
                            }
                        }
                    },
                    scaffoldState = scaffoldState
                )
            }

        }
    }
}