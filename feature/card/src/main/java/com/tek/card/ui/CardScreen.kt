package com.tek.card.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tek.card.presentation.CardState
import com.tek.card.presentation.CrudCardViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.Screen
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
import org.koin.androidx.compose.get

@Composable
fun CardScreen(navController: NavController) {
    val viewModel = get<CrudCardViewModel>()
    val state =
        viewModel.cardState.collectAsStateWithLifecycle(initialValue = CardState.Initial).value
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
        },
        floatingActionButton = {
            if (state is CardState.Loaded && state.data.isNotEmpty()) {
                FloatingActionButton(
                    modifier = Modifier
                        .testTag("addFab"),
                    onClick = { navController.navigate(Screen.AddCardScreen.route) },
                    shape = CircleShape,
                    containerColor = HolviTheme.colors.primaryBackground
                ) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "add",
                        tint = HolviTheme.colors.primaryForeground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) {
        when (state) {
            is CardState.Loaded -> {
                if (state.data.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No cards found.",
                            color = HolviTheme.colors.appForeground,
                            style = HolviTheme.typography.body
                        )
                        Text(
                            text = "Would you like to add one?",
                            color = HolviTheme.colors.appForeground,
                            style = HolviTheme.typography.body
                        )
                        Button(modifier = Modifier
                            .testTag("addCardButton"),
                            colors = holviButtonColors(),
                            onClick = {
                                navController.navigate(Screen.AddCardScreen.route)
                            }) {
                            Text(text = "Add", style = HolviTheme.typography.body)
                        }
                    }
                } else {
                    CardStack(
                        modifier = Modifier.padding(top = it.calculateTopPadding()),
                        cards = state.data
                    )
                }
            }

            else -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = HolviTheme.colors.appForeground)
            }
        }
    }
}