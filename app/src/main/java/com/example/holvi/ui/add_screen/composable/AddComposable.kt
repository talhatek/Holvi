package com.example.holvi.ui.add_screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
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
import com.example.holvi.utils.PasswordManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AddScreen(navController: NavController) {
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
                    navController.popBackStack()
                    myAddViewModel.passwordUiHint.value = ""

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


@Composable
fun InputView(hintParam: String, onValueChanged: (input: String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }

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
    addViewModel: AddViewModel,
    hintParam: String,
    onValueChanged: (input: String) -> Unit
) {

    var hint by remember { mutableStateOf(hintParam) }
    var data by remember { addViewModel.passwordUiHint }
    TextField(
        value = data,
        onValueChange = {
            data = it
            onValueChanged.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
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

        textStyle = TextStyle(
            fontFamily = PoppinsRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,

            ),
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (data.isEmpty())
                        hint = ""
                } else
                    if (data.isEmpty())
                        hint = hintParam
            },
        singleLine = true
    )

}

