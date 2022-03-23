package com.example.holvi.ui.authenticationActivity.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.HolviApp
import com.example.holvi.ui.authenticationActivity.AuthenticationViewModel
import com.example.holvi.ui.authenticationActivity.SQState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@Composable
fun AuthenticationMainScreen(onClick: () -> Unit, onMessageDeliver: (message: String) -> Unit) {
    val viewModel = get<AuthenticationViewModel>()
    val context = LocalContext.current
    var buttonEnabledState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true, block = {
        viewModel.sqState.collectLatest {
            when (it) {
                is SQState.Success -> {
                    (context.applicationContext as HolviApp).initSq(it.id)
                    buttonEnabledState = true
                }
                is SQState.Error -> {
                    onMessageDeliver.invoke(it.message)
                }
                else -> Unit
            }
        }
    })
    HolviTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = SpaceAround,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text = "Welcome Back!", color = Color.White, style = MaterialTheme.typography.h4)
            Text(
                text = "Please authenticate to continue...",

                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            Button(onClick = {
                onClick.invoke()
            }, enabled = buttonEnabledState, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = "Authenticate",
                    Modifier.padding(4.dp),
                    style = MaterialTheme.typography.body1
                )
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

}