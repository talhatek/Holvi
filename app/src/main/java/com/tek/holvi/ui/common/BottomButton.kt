package com.tek.holvi.ui.common

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tek.holvi.theme.PoppinsLight
import com.tek.holvi.theme.PrimaryTextColor
import com.tek.holvi.theme.SecondPrimary


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
            style = TextStyle(
                color = SecondPrimary,
                fontWeight = FontWeight.Light,
                fontFamily = PoppinsLight,
                fontSize = 24.sp
            ),
            color = PrimaryTextColor
        )

    }
}
