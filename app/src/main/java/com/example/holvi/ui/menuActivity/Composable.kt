package com.example.holvi.ui.menuActivity


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            Text(
                text = "What you wanna do?",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            BoxWithConstraints {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(((this.maxHeight / 100) * 10)),
                    horizontalAlignment = CenterHorizontally,

                    ) {
                    items(itemList) { menuItem ->
                        MenuItem(title = menuItem) { selectedTitle ->
                            onItemSelected.invoke(selectedTitle)
                        }
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