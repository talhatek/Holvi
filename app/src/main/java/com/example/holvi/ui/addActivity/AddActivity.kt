package com.example.holvi.ui.addActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.holvi.di.AddViewModel
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.addActivity.composable.InputView
import com.example.holvi.ui.addActivity.composable.PasswordInputView
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleTextButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.utils.MenuType.Companion.ADD
import com.example.holvi.utils.PasswordManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

class AddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(
                    topBar = {
                        TopAppBarBackWithLogo {
                            this.finish()
                        }
                    },
                    bottomBar = {
                        BottomButton(text = ADD)
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.8f),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val myAddViewModel = get<AddViewModel>()
                            val passwordManager = PasswordManager()
                            InputView(hintParam = "Site Name") {

                            }

                            InputView(hintParam = "User Name") {

                            }

                            PasswordInputView(myAddViewModel, hintParam = "Password") {

                            }
                            CircleTextButton(text = "G", percentage = 20) {
                                CoroutineScope(Dispatchers.Default).launch {
                                    myAddViewModel.text.value =
                                        passwordManager.generatePassword(length = 8)
                                }

                            }
                        }
                    }
                )
            }

        }
    }
}