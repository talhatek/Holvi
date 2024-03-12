package com.tek.card.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tek.card.presentation.CardViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.TopAppBarBackWithLogo
import org.koin.androidx.compose.get

@Composable
fun CardScreen(navController: NavController) {
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo {
                navController.popBackStack()
            }
        },
    ) {
        val viewModel = get<CardViewModel>()
        CardStack(modifier = Modifier.padding(top = it.calculateTopPadding()))
    }
}