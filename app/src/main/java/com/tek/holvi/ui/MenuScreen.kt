package com.tek.holvi.ui


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.holvi.R
import com.tek.holvi.utils.MenuType
import com.tek.holvi.utils.Screen
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarOnlyIcon
import com.tek.ui.holviButtonColors

@Composable
fun MenuScreen(navController: NavController, onExitClick: () -> Unit) {
    HolviScaffold(
        topBar = {
            TopAppBarOnlyIcon(res = R.drawable.ic_power) {
                onExitClick.invoke()
            }
        },
        content = {
            MenuScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                itemList = mutableListOf<@MenuType String>(
                    MenuType.CARD,
                    MenuType.PASSWORD,
                )
            ) { menuType ->
                when (menuType) {
                    MenuType.CARD -> {
                        navController.navigate(Screen.CardScreen.route)
                    }

                    MenuType.PASSWORD -> {
                        navController.navigate(Screen.PasswordScreen.route)
                    }

                }
            }
        }
    )
}

@Composable
fun MenuScreenContent(
    modifier: Modifier,
    @MenuType itemList: List<String>,
    onItemSelected: (title: String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        Text(
            text = "What do you need to see?",
            style = HolviTheme.typography.body,
            color = HolviTheme.colors.appForeground,
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
    @MenuType title: String,
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
