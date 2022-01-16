package com.example.holvi.ui.generateActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.addActivity.composable.InputView
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.CircleIconButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.deleteActivity.composable.HolviDropdown
import com.example.holvi.ui.generateActivity.composable.HolviGenerateSwitch
import org.koin.androidx.compose.get

class GenerateActivity : ComponentActivity() {

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
                        BottomButton(text = "Copy to clipboard") {

                        }
                    }
                ) {
                    val viewModel = get<GenerateViewModel>()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp),
                                    text = viewModel.currentPassword.value
                                )
                                Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight(.1f)
                                .fillMaxWidth()
                        )
                        CircleIconButton(iconIdRes = R.drawable.ic_renew, percentage = 20) {
                            viewModel.generatePassword()
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight(.1f)
                                .fillMaxWidth()
                        )
                        InputView(hintParam = "Forbidden") {

                        }
                        HolviDropdown(data = viewModel.dropdownItems, "Choose password length") {
                            viewModel.currentSelectedLength.value = it
                        }


                        HolviGenerateSwitch(switchText = "Symbols") {
                            with(viewModel) {
                                updateActiveCount(it)
                                symbolState.value = it
                            }

                        }
                        HolviGenerateSwitch(switchText = "Numbers") {
                            with(viewModel) {
                                updateActiveCount(it)
                                numberState.value = it
                            }

                        }
                        HolviGenerateSwitch(switchText = "Upper Case") {
                            with(viewModel) {
                                updateActiveCount(it)
                                upperCaseState.value = it
                            }

                        }
                        HolviGenerateSwitch(switchText = "Lower Case") {
                            with(viewModel) {
                                updateActiveCount(it)
                                lowerCaseState.value = it
                            }

                        }

                    }

                }
            }
        }
    }
}