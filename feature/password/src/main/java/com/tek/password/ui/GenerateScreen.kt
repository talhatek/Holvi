package com.tek.password.ui

import android.annotation.SuppressLint
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.password.R
import com.tek.password.di.getViewModelScope
import com.tek.password.presentation.GenerateViewModel
import com.tek.password.presentation.GenerateViewUiEvent
import com.tek.ui.BottomButton
import com.tek.ui.CircleIconButton
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun GenerateScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel = koinViewModel<GenerateViewModel>(
        scope = getKoin()
            .getViewModelScope(GenerateViewModel.SCOPE_NAME)
    )
    val currentPassword = viewModel.currentPassword.collectAsState().value
    val dropdownItems = viewModel.dropdownItems.collectAsState().value
    val lengthSelectorText = viewModel.lengthSelectorText.collectAsState().value
    val blurDp = remember {
        mutableStateOf(0.dp)
    }

    val blurAnimate = animateDpAsState(targetValue = blurDp.value, label = "background blur dp")
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is GenerateViewUiEvent.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    HolviScaffold(
        modifier = Modifier.blur(blurAnimate.value, blurAnimate.value),
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
        },
        bottomBar = {
            val context = LocalContext.current
            BottomButton(text = "Copy to clipboard") {
                viewModel.copyToClipBoard(context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
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
                            .padding(vertical = 4.dp)
                            .alpha(if (currentPassword.isBlank()) .4f else 1f),
                        text = currentPassword.ifBlank { "Your password will be displayed here." },
                        color = HolviTheme.colors.appForeground,
                        style = HolviTheme.typography.body,
                    )
                    HorizontalDivider(
                        Modifier.fillMaxWidth(.7f), color = HolviTheme.colors.appForeground
                    )
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
            SimpleInputView(hintParam = "Please enter your forbidden chars.") {
                viewModel.forbiddenLetters.value = it
            }
            Spacer(modifier = Modifier.height(16.dp))

            HolviDropdown(
                data = dropdownItems,
                defaultHint = lengthSelectorText,
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
                    .alpha(.7f)
                    .fillMaxWidth(),
                style = HolviTheme.typography.body,
                color = HolviTheme.colors.appForeground
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = HolviTheme.colors.appForeground,
            unfocusedTextColor = HolviTheme.colors.appForeground,
            focusedIndicatorColor = HolviTheme.colors.appForeground,
            unfocusedIndicatorColor = HolviTheme.colors.appForeground,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = HolviTheme.colors.primaryBackground
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
            color = HolviTheme.colors.appForeground,
            style = HolviTheme.typography.body,
        )
        Switch(
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
                checkedTrackColor = HolviTheme.colors.primaryBackground,
                checkedThumbColor = HolviTheme.colors.primaryForeground
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
    defaultHint: String,
    onItemSelected: (length: Int) -> Unit,
    onExpanded: (isExpanded: Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTmpData by remember { mutableStateOf(defaultHint) }

    LaunchedEffect(expanded) {
        onExpanded.invoke(expanded)
    }

    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.clickable {
        expanded = !expanded
    }) {
        Text(
            text = selectedTmpData,
            style = HolviTheme.typography.body,
            color = HolviTheme.colors.appForeground
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand collapse",
            tint = HolviTheme.colors.appForeground,
            modifier = Modifier
                .size(24.dp)
                .align(alignment = CenterVertically)
        )
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(surface = HolviTheme.colors.appBackground),
        ) {
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier
                    .heightIn(max = 440.dp),
                onDismissRequest = {
                    expanded = false
                }) {
                data
                    .filterNot { it.toString() == selectedTmpData }
                    .forEach {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                selectedTmpData = it.toString()
                                onItemSelected.invoke(it)
                            },
                            text = {
                                Text(
                                    text = it.toString(),
                                    style = HolviTheme.typography.body,
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    color = HolviTheme.colors.appForeground
                                )
                            }
                        )
                    }
            }
        }

    }
}




