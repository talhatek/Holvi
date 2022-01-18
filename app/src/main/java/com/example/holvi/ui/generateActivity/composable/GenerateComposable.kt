package com.example.holvi.ui.generateActivity.composable

import android.annotation.SuppressLint
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleIconButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.delete_screen.composable.HolviDropdown
import com.example.holvi.ui.generateActivity.GenerateViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun GenerateScreen(navController: NavController) {
    HolviTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val viewModel = get<GenerateViewModel>()
        val forbiddenHint = mutableStateOf("Forbidden")
        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collectLatest {
                when (it) {
                    is GenerateViewModel.GenerateViewUiEvent.SnackbarEvent -> {
                        scaffoldState.snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBarBackWithLogo {
                    navController.popBackStack()
                }
            },
            bottomBar = {
                val context = LocalContext.current
                BottomButton(text = "Copy to clipboard") {
                    viewModel.copyToClipBoard(context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager)
                }
            },
            scaffoldState = scaffoldState
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            text = viewModel.currentPassword.value
                        )
                        Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(.05f)
                        .fillMaxWidth()
                )
                CircleIconButton(iconIdRes = R.drawable.ic_renew, percentage = 15) {
                    viewModel.generatePassword()
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(.05f)
                        .fillMaxWidth()
                )
                SimpleInputView(hintParam = "Forbidden") {
                    viewModel.forbiddenLetters.value = it
                }
                HolviDropdown(
                    data = viewModel.dropdownItems,
                    viewModel.lengthSelectorText
                ) {
                    viewModel.currentSelectedLength.value = it
                    viewModel.lengthSelectorText.value = it.toString()
                }


                HolviGenerateSwitch(switchText = "Symbols") {
                    with(viewModel) {
                        updateActiveCount(it)
                        symbolState.value = it
                    }

                }
                HolviGenerateSwitch(switchText = "Numbers") {
                    with(viewModel) {
                        updateActiveCount(it)
                        numberState.value = it
                    }

                }
                HolviGenerateSwitch(switchText = "Upper Case") {
                    with(viewModel) {
                        updateActiveCount(it)
                        upperCaseState.value = it
                    }

                }
                HolviGenerateSwitch(switchText = "Lower Case") {
                    with(viewModel) {
                        updateActiveCount(it)
                        lowerCaseState.value = it
                    }

                }

            }

        }
    }
}

@Composable
fun SimpleInputView(
    hintParam: String,
    onValueChanged: (input: String) -> Unit
) {
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
fun HolviGenerateSwitch(switchText: String, isChecked: (state: Boolean) -> Unit) {
    val checkedState = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = switchText)
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it; isChecked.invoke(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = Color.Black,
                uncheckedTrackColor = Color.White,
            )
        )

    }
}
