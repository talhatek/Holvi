package com.example.holvi.ui.authenticationActivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.authenticationActivity.composable.AuthenticationMainScreen
import com.example.holvi.ui.menu_screen.MenuActivity
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
class AuthenticationActivity : FragmentActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricManager: BiometricManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biometricManager = BiometricManager.from(this)

        biometricPrompt = BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Toast.makeText(this@AuthenticationActivity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                startActivity(Intent(this@AuthenticationActivity, MenuActivity::class.java))
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(this@AuthenticationActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
        biometricInfo = BiometricPrompt.PromptInfo.Builder()
            .setNegativeButtonText("Cancel")
            .setTitle("Authenticate")
            .setDescription("Please authenticate to continue")
            .build()
        setContent {
            HolviTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    content = {
                        AuthenticationMainScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = it.calculateTopPadding()),
                            onClick = {
                                startActivity(
                                    Intent(this, MenuActivity::class.java),
                                    ActivityOptions.makeCustomAnimation(
                                        this,
                                        R.anim.slide_forward,
                                        R.anim.slide_backward
                                    ).toBundle()
                                )

                            }, onMessageDeliver = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = it)
                                }
                            })
                    }
                )
            }
        }
    }
}

