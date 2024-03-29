package com.tek.card.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.card.presentation.CardState
import com.tek.card.presentation.CrudCardViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.Screen
import com.tek.ui.TopAppBarBackWithLogo
import org.koin.androidx.compose.get

@Composable
fun CardScreen(navController: NavController) {
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .testTag("addFab"),
                onClick = { navController.navigate(Screen.AddPasswordScreen.route) },
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
    ) {
        val viewModel = get<CrudCardViewModel>()
        when (val state = viewModel.cardState.collectAsState().value) {
            is CardState.Loaded -> {
                CardStack(
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    cards = state.data
                )
            }

            else -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = HolviTheme.colors.appForeground)
            }
        }
    }
}