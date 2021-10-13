package com.example.holvi.ui.authenticationActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.authenticationActivity.composable.AuthenticationMainScreen
import com.example.holvi.ui.common.composable.CircleTextButton

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(
                    /*   topBar = {
                           TopAppBarOnlyIcon(res = R.drawable.ic_power){

                           }
                       },
                       bottomBar = {
                           BottomButton(text = "Save")
                       },*/
                    content = {
                        AuthenticationMainScreen()
                    }
                )
            }
        }
    }
}


//@Preview(showBackground = true,showSystemUi = true)
@Composable
fun DefaultPreview() {
    HolviTheme {
        CircleTextButton("G", 40) {}
    }
}
