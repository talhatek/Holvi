package com.example.holvi.ui.authenticationActivity.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme

@Preview(showSystemUi = true)
@Composable
fun AuthenticationMainScreen() {
    HolviTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black, shape = RectangleShape),
            verticalArrangement = SpaceAround,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text = "Welcome Back!", color = Color.White, style = MaterialTheme.typography.h4)
            Text(
                text = "Please authenticate to continue...",

                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            Button(onClick = {}, shape = RoundedCornerShape(8.dp)) {
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