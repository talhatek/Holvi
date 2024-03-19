package com.tek.card.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tek.card.presentation.CardState
import com.tek.card.presentation.CardViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import org.koin.androidx.compose.get

@Composable
fun CardScreen(navController: NavController) {
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
        },
    ) {
        val viewModel = get<CardViewModel>()
        when (val state = viewModel.cards.value) {
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