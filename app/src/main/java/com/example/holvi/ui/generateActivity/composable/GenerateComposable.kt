package com.example.holvi.ui.generateActivity.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun HolviGenerateSwitch(switchText: String) {
    val checkedState = remember { mutableStateOf(true) }
    Row(modifier = Modifier.fillMaxWidth(.8f), horizontalArrangement = Arrangement.SpaceAround) {
        Text(text = switchText)
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = Color.Black,
                uncheckedTrackColor = Color.White,
            )
        )

    }
}
