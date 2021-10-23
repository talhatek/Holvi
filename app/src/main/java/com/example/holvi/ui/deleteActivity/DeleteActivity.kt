package com.example.holvi.ui.deleteActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.deleteActivity.composable.HolviDropdown

class DeleteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(topBar = {
                    TopAppBarBackWithLogo {
                        this.finish()
                    }
                },
                    bottomBar = {
                        BottomButton(text = "Delete") {

                        }
                    }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            HolviDropdown(listOf())
                            Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}