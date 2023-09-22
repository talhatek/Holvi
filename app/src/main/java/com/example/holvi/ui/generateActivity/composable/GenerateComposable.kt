package com.example.holvi.ui.generateActivity.composable

import android.annotation.SuppressLint
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.di.getViewModelScope
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleIconButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.generateActivity.GenerateViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun GenerateScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<GenerateViewModel>(
        scope = getKoin()
            .getViewModelScope(GenerateViewModel.SCOPE_NAME)
    )
    val forbiddenHint = mutableStateOf("Forbidden")
    val blurDp = remember {
        mutableStateOf(0.dp)
    }

    val blurAnimate = animateDpAsState(targetValue = blurDp.value, label = "background blur dp")
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is GenerateViewModel.GenerateViewUiEvent.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.blur(blurAnimate.value, blurAnimate.value),
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
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
                        text = viewModel.currentPassword.value,
                        color = Color.White,
                        style = TextStyle(textAlign = TextAlign.Center)
                    )
                    Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxHeight(.05f)
                    .fillMaxWidth()
            )
            CircleIconButton(iconIdRes = R.drawable.ic_renew) {
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
                defaultHint = viewModel.lengthSelectorText,
                onExpanded = { expanded ->
                    blurDp.value = if (expanded) {
                        4.dp
                    } else {
                        0.dp
                    }
                },
                onItemSelected = {
                    viewModel.currentSelectedLength.value = it
                    viewModel.lengthSelectorText.value = it.toString()
                }
            )

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
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
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
fun HolviGenerateSwitch(
    switchText: String,
    isChecked: (state: Boolean) -> Unit
) {
    val viewModel =
        koinViewModel<GenerateViewModel>(scope = getKoin().getViewModelScope(GenerateViewModel.SCOPE_NAME))
    val checkedState = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = switchText,
            Modifier
                .fillMaxWidth(.5f),
            color = Color.White
        )
        Switch(
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
            ),
            checked = checkedState.value,
            onCheckedChange = {
                if ((viewModel.activeCount.value == 1) and !it) {
                    scope.launch {
                        checkedState.value = it
                        delay(100)
                        checkedState.value = true
                    }
                } else {
                    checkedState.value = it
                    isChecked.invoke(it)
                }
            })
    }
}

@Composable
fun HolviDropdown(
    data: List<Int>,
    defaultHint: MutableState<String>,
    onItemSelected: (length: Int) -> Unit,
    onExpanded: (isExpanded: Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = expanded, block = {
        onExpanded.invoke(expanded)
    })

    val tmpData = data.toMutableList()
    var selectedTmpData by remember { defaultHint }
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.clickable {
        expanded = !expanded
    }) { // Anchor view
        Text(
            text = selectedTmpData, style = TextStyle(
                fontFamily = PoppinsRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.White
            )
        ) // City name label
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            "Expand collapse",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .align(alignment = CenterVertically)
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(SecondPrimary)
                .heightIn(max = 440.dp),
            onDismissRequest = {
                expanded = false
            }) {
            tmpData.forEach {
                if (selectedTmpData == it.toString())
                    return@forEach
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedTmpData = it.toString()
                        onItemSelected.invoke(it)
                    },
                    text = {
                        Text(
                            text = it.toString(),
                            style = TextStyle(
                                fontFamily = PoppinsRegular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                )
            }
        }
    }
}




