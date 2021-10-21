package com.example.holvi.ui.allActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.holvi.db.Password
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.allActivity.composable.PasswordItem
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo

class AllActivity : ComponentActivity() {
    private val mock = emptyList<Password>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(topBar = {
                    TopAppBarBackWithLogo {
                        this.finish()
                    }
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        content = {
                            items(mock) { item ->
                                PasswordItem(password = item)
                            }

                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                        contentPadding = PaddingValues(top = 16.dp)

                    )
                }

            }
        }
    }
}