package com.tek.holvi.ui.menuActivity


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.holvi.R
import com.tek.holvi.theme.OnBackgroundTextColor
import com.tek.holvi.theme.PoppinsRegular
import com.tek.holvi.theme.PoppinsSemiBold
import com.tek.holvi.theme.PrimaryTextColor
import com.tek.holvi.utils.MenuType
import com.tek.holvi.utils.Screen
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarOnlyIcon

@Composable
fun MenuScreen(navController: NavController, onExitClick: () -> Unit) {
    Scaffold(topBar = {
        TopAppBarOnlyIcon(res = R.drawable.ic_power) {
            onExitClick.invoke()
        }
    },
        content = {
            MenuScreenContent(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = it.calculateTopPadding()),
                itemList = mutableListOf<@MenuType String>(
                    MenuType.SEE_ALL,
                    MenuType.GENERATE,
                    MenuType.PORT
                )
            ) { menuType ->
                when (menuType) {
                    MenuType.SEE_ALL -> {
                        navController.navigate(Screen.AllScreen.route)
                    }

                    MenuType.GENERATE -> {
                        navController.navigate(Screen.GenerateScreen.route)
                    }

                    MenuType.PORT -> {
                        navController.navigate(Screen.PortScreen.route)
                    }
                }
            }
        }
    )
}

@Composable
fun MenuScreenContent(
    modifier: Modifier,
    @PreviewParameter(MenuProvider::class) @MenuType itemList: List<String>,
    onItemSelected: (title: String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        Text(
            text = "What you wanna do?",
            style = MaterialTheme.typography.bodyLarge,
            color = OnBackgroundTextColor,
            fontFamily = PoppinsRegular
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        BoxWithConstraints {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Arrangement.spacedBy(this.maxHeight.div(10))
                } else {
                    Arrangement.SpaceEvenly
                },
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
            Text(
                text = title,
                modifier = Modifier.padding(all = 8.dp),
                color = PrimaryTextColor,
                fontFamily = PoppinsSemiBold
            )
        }
    }
}

class MenuItemProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("Add")
}

class MenuProvider : PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>>
        get() = sequenceOf(listOf("Add", "Update", "See All", "Generate"))

}