package com.example.holvi.ui.add_screen.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.holvi.db.model.Password
import com.example.holvi.theme.HolviTheme
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.ui.add_screen.AddPasswordState
import com.example.holvi.ui.add_screen.AddViewModel
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleTextButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.utils.MenuType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AddScreen(navController: NavController) {
    HolviTheme {
        val scaffoldState = rememberScaffoldState()
        val myAddViewModel = get<AddViewModel>()
        val scope = rememberCoroutineScope()
        var siteName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var userName by remember { mutableStateOf("") }
        LaunchedEffect(key1 = true) {
            myAddViewModel.passwordAddState.collect {
                when (it) {
                    is AddPasswordState.Success -> {
                        scaffoldState.snackbarHostState.showSnackbar("Password added successfully.")
                    }
                    is AddPasswordState.Failure -> {
                        scaffoldState.snackbarHostState.showSnackbar(it.message)
                    }
                    else -> Unit
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBarBackWithLogo {
                    myAddViewModel.clearPassword()
                    navController.popBackStack()
                }
            },
            bottomBar = {
                BottomButton(text = MenuType.ADD) {
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
                BoxWithConstraints {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.8f),
                        verticalArrangement = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) Arrangement.spacedBy(
                            ((this.maxHeight / 100) * 10)
                        ) else Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        item {
                            InputView(hintParam = "Site Name", viewModel = myAddViewModel) {
                                siteName = it
                            }

                        }
                        item {
                            InputView(hintParam = "User Name", viewModel = myAddViewModel) {
                                userName = it

                            }
                        }
                        item {
                            PasswordInputView(hintParam = "Password", viewModel = myAddViewModel) {
                                password = it
                            }
                        }
                        item {
                            CircleTextButton(
                                text = "G",
                                percentage = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 10 else 20
                            ) {
                                password = myAddViewModel.generatePassword()
                            }
                        }


                    }
                }

            },
            scaffoldState = scaffoldState
        )
    }

}

@Composable
fun InputView(hintParam: String, viewModel: AddViewModel, onValueChanged: (input: String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }
    LaunchedEffect(key1 = true, block = {
        viewModel.clearInputsSharedFlow.collectLatest {
            if (it == 1) {
                value = ""

            }
        }
    })
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
                    .alpha(.5f)
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = PoppinsRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,

                    ),
                color = Color.White
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (value.isEmpty())
                        hint = ""
                } else
                    if (value.isEmpty())
                        hint = hintParam
            },
        singleLine = true
    )

}

@Composable
fun PasswordInputView(
    viewModel: AddViewModel,
    hintParam: String,
    onValueChanged: (input: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }

    LaunchedEffect(key1 = true, block = {
        viewModel.clearInputsSharedFlow.collectLatest {
            if (it == 1) {
                value = ""
                focusManager.clearFocus()
            }
        }
    })
    LaunchedEffect(key1 = true, block = {
        viewModel.passwordStateFlow.collectLatest {
            if (it.isNotEmpty()) {
                value = it
                focusManager.clearFocus()
            }
        }
    })


    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
                    .alpha(.5f)
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                style = TextStyle(
                    fontFamily = PoppinsRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,

                    ),
                color = Color.White
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (value.isEmpty())
                        hint = ""
                } else
                    if (value.isEmpty())
                        hint = hintParam
            },
        singleLine = true
    )

}

