package com.tek.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MenuItem(
    title: String,
    onClicked: (title: String) -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(0.7f),
        onClick = { onClicked.invoke(title) },
        shape = RoundedCornerShape(16.dp),
        colors = holviButtonColors()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(all = 8.dp),
            style = HolviTheme.typography.body
        )
    }
}
