package com.example.holvi.ui.authenticationActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleTextButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(
                    topBar = {
                        TopAppBarBackWithLogo()
                    },
                    bottomBar = { BottomButton(text = "Save") }
                ) {

                }

            }
        }
    }
}


//@Preview(showBackground = true,showSystemUi = true)
@Composable
fun DefaultPreview() {
    HolviTheme {
        CircleTextButton("G")
    }
}
