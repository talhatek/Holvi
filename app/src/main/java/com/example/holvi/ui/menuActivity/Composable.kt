package com.example.holvi.ui.menuActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.extension.MenuType


@Composable
fun MenuMainScreen(
    @PreviewParameter(MenuProvider::class) @MenuType itemList: List<String>,
    onItemSelected: (title: String) -> Unit
) {
    HolviTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "What you wanna do?",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = CenterHorizontally
            ) {
                itemList.forEach {
                    MenuItem(title = it) { selectedTitle ->
                        onItemSelected.invoke(selectedTitle)
                    }
                }
            }

        }
    }
}


@Composable
fun MenuItem(
    @PreviewParameter(MenuItemProvider::class) @MenuType title: String,
    onClicked: (title: String) -> Unit
) {
    HolviTheme {
        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = { onClicked.invoke(title) },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = title, modifier = Modifier.padding(all = 8.dp))
        }
    }

}

class MenuItemProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("Add")
}

class MenuProvider : PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>>
        get() = sequenceOf(listOf("Add", "Update", "See All", "Delete", "Generate"))

}