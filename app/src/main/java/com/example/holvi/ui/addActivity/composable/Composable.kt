package com.example.holvi.ui.addActivity.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun InputView(hintParam: String, onValueChanged: () -> Unit) {
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke()
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
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
