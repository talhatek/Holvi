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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.ui.addActivity.AddViewModel

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

