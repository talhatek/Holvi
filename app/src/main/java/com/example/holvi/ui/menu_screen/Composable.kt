package com.example.holvi.ui.menu_screen


import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.common.composable.TopAppBarOnlyIcon
import com.example.holvi.utils.MenuType
import com.example.holvi.utils.Screen

@Composable
fun MenuScreen(navController: NavController, onExitClick: () -> Unit) {
    HolviTheme {
        Scaffold(topBar = {
            TopAppBarOnlyIcon(res = R.drawable.ic_power) {
                onExitClick.invoke()
            }
        },
            content = {
                MenuScreenContent(
                    itemList = mutableListOf<@MenuType String>(
                        MenuType.ADD,
                        MenuType.SEE_ALL,
                        MenuType.DELETE,
                        MenuType.GENERATE
                    )
                ) {
                    when (it) {
                        MenuType.ADD -> {
                            navController.navigate(Screen.AddScreen.route)

                        }
                        MenuType.SEE_ALL -> {
                            navController.navigate(Screen.AllScreen.route)

                        }
                        MenuType.DELETE -> {
                            navController.navigate(Screen.DeleteScreen.route)


                        }
                        MenuType.GENERATE -> {
                            navController.navigate(Screen.GenerateScreen.route)

                        }

                    }
                }
            }

        )
    }
}

@Composable
fun MenuScreenContent(
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
            BoxWithConstraints() {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) Arrangement.spacedBy(
                        ((this.maxHeight / 100) * 10)
                    ) else Arrangement.SpaceEvenly,
                    horizontalAlignment = CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp)

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