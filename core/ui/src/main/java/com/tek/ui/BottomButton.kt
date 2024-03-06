package com.tek.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun BottomButton(text: String, onClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f), onClick = {
            onClicked.invoke()
        }, shape = RectangleShape
    ) {
        Text(
            text = text,
            style = HolviTheme.typography.title,
            color = HolviTheme.colors.primaryTextColor
        )

    }
}
