package com.tek.holvi.ui

import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tek.holvi.R
import com.tek.holvi.presentation.AuthenticationViewModel
import com.tek.holvi.presentation.SQState
import com.tek.ui.HolviTheme
import com.tek.ui.holviButtonColors
import org.koin.androidx.compose.get

@Composable
fun AuthenticationMainScreen(
    modifier: Modifier,
    onClick: () -> Unit,
    onMessageDeliver: (message: String) -> Unit
) {

    val viewModel = get<AuthenticationViewModel>()
    val sqState = viewModel.sqState.collectAsState().value

    var buttonEnabledState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(sqState) {
        when (sqState) {
            is SQState.Success -> {
                buttonEnabledState = true
            }

            is SQState.Error -> {
                onMessageDeliver.invoke(sqState.message)
            }

            else -> Unit
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = SpaceAround,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Welcome Back!",
            color = HolviTheme.colors.appForeground,
            style = HolviTheme.typography.largestHeader,
        )
        Text(
            text = "Please authenticate to continue...",
            color = HolviTheme.colors.appForeground,
            style = HolviTheme.typography.title,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(.52f)
                .height(52.dp),
            onClick = {
                onClick()
            }, enabled = buttonEnabledState,
            shape = RoundedCornerShape(8.dp),
            colors = holviButtonColors()
        ) {
            if (buttonEnabledState) {
                Text(
                    text = "Authenticate",
                    style = HolviTheme.typography.title,
                    color = HolviTheme.colors.primaryForeground,

                    )
            } else {
                CircularProgressIndicator(
                    Modifier
                        .size(16.dp),
                    strokeWidth = 4.dp,
                    color = HolviTheme.colors.primaryForeground,
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            contentDescription = "lock",
            Modifier
                .fillMaxWidth(.6f)
                .fillMaxHeight(.4f),
            tint = HolviTheme.colors.primaryBackground
        )
    }
}
