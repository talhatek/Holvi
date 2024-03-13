package com.tek.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreenContent(
    modifier: Modifier,
    itemList: List<Screen>,
    onItemSelected: (title: String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
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
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp)

            ) {
                items(itemList) { menuItem ->
                    MenuItem(title = menuItem.route) { selectedTitle ->
                        onItemSelected.invoke(selectedTitle)
                    }
                }
            }
        }
    }
}

