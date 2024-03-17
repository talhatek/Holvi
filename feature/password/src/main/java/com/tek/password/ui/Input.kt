package com.tek.password.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.ClearFocus
import com.tek.ui.HolviTheme

@Composable
fun Input(
    defaultValue: String = "",
    label: String,
    viewModel: AddViewModel,
    onValueChanged: (input: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf(defaultValue) }

    LaunchedEffect(Unit) {
        viewModel.clearInputsSharedFlow.collect {
            if (it is ClearFocus.Clear) {
                focusManager.clearFocus()
                value = ""
            }
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .testTag(label)
            .fillMaxWidth(.7f),
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke(it)
        },
        label = {
            Text(
                text = label,
                style = HolviTheme.typography.body,
                color = HolviTheme.colors.appForeground,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = HolviTheme.colors.appForeground,
            unfocusedTextColor = HolviTheme.colors.appForeground,
            focusedIndicatorColor = HolviTheme.colors.appForeground,
            unfocusedIndicatorColor = HolviTheme.colors.appForeground,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = HolviTheme.colors.primaryBackground,
        ),
        singleLine = true
    )
}