package com.example.holvi.ui.authenticationActivity.composable

import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.holvi.R
import com.example.holvi.ui.HolviApp
import com.example.holvi.ui.authenticationActivity.AuthenticationViewModel
import com.example.holvi.ui.authenticationActivity.SQState
import org.koin.androidx.compose.get

@Composable
fun AuthenticationMainScreen(
    modifier: Modifier,
    onClick: () -> Unit, onMessageDeliver: (message: String) -> Unit
) {

    val viewModel = get<AuthenticationViewModel>()
    val sqState = viewModel.sqState.collectAsState().value

    val context = LocalContext.current

    var buttonEnabledState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(sqState) {
        when (sqState) {
            is SQState.Success -> {
                (context.applicationContext as HolviApp).initSq(sqState.id)
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
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Please authenticate to continue...",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(.52f)
                .height(52.dp),
            onClick = {
                onClick.invoke()
            }, enabled = buttonEnabledState,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(.4f),
                disabledContentColor = Color.White.copy(alpha = .2f)
            )
        ) {
            if (buttonEnabledState) {
                Text(
                    text = "Authenticate",
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                CircularProgressIndicator(
                    Modifier
                        .size(16.dp),
                    strokeWidth = 4.dp
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            contentDescription = "lock",
            Modifier
                .fillMaxWidth(.6f)
                .fillMaxHeight(.4f),
            tint = Color.White
        )
    }
}
