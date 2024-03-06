package com.tek.password.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.ClearFocus
import com.tek.ui.HolviTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InputView(
    hintParam: String,
    defaultValue: String? = null,
    viewModel: AddViewModel,
    onValueChanged: (input: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf(defaultValue.orEmpty()) }
    var hint by remember { mutableStateOf(hintParam) }
    val clearEvent = viewModel.clearInputsSharedFlow.collectAsState(initial = ClearFocus.Init).value
    LaunchedEffect(key1 = clearEvent, block = {
        if (clearEvent is ClearFocus.Clear) {
            focusManager.clearFocus()
            value = ""
            hint = hintParam
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
                    .fillMaxWidth(),
                style = HolviTheme.typography.body
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = HolviTheme.colors.primaryTextColor,
            unfocusedTextColor = HolviTheme.colors.primaryTextColor,
            focusedIndicatorColor = HolviTheme.colors.primaryTextColor,
            unfocusedIndicatorColor = HolviTheme.colors.primaryTextColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),

        textStyle = HolviTheme.typography.body,
        modifier = Modifier
            .testTag(hintParam)
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
    defaultValue: String? = null,
    onValueChanged: (input: String) -> Unit,
) {
    val color = MaterialTheme.colorScheme.primary

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf(defaultValue.orEmpty()) }
    var hint by remember { mutableStateOf(hintParam) }
    val clearEvent = viewModel.clearInputsSharedFlow.collectAsState(initial = ClearFocus.Init).value

    LaunchedEffect(key1 = clearEvent, block = {
        if (clearEvent is ClearFocus.Clear) {
            focusManager.clearFocus()
            value = ""
            hint = hintParam
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
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                style = HolviTheme.typography.body,
                color = HolviTheme.colors.primaryTextColor,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = HolviTheme.colors.primaryTextColor,
            unfocusedTextColor = HolviTheme.colors.primaryTextColor,
            focusedIndicatorColor = HolviTheme.colors.primaryTextColor,
            unfocusedIndicatorColor = HolviTheme.colors.primaryTextColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        textStyle = HolviTheme.typography.title,
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (value.isEmpty())
                        hint = ""
                } else
                    if (value.isEmpty())
                        hint = hintParam
            }
            .testTag("PasswordTextField"),
        singleLine = true,
        trailingIcon = {
            Box(
                modifier = Modifier
                    .testTag("generateIcon")
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        color = color,
                        shape = CircleShape,
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = Color.Black,
                            radius = 24.dp
                        ),
                        onClick = {
                            value = viewModel
                                .generatePassword()
                                .also(onValueChanged)

                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "G",
                    color = Color.Black,
                    style = TextStyle.Default.copy(
                        fontSize = 12.dp.value.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    )

}

