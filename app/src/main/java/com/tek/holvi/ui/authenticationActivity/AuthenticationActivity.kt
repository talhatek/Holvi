package com.tek.holvi.ui.authenticationActivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.tek.holvi.BuildConfig
import com.tek.holvi.R
import com.tek.holvi.theme.HolviTheme
import com.tek.holvi.ui.menuActivity.MenuActivity
import kotlinx.coroutines.launch

class AuthenticationActivity : FragmentActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricManager: BiometricManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biometricManager = BiometricManager.from(this)

        biometricPrompt = BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Toast.makeText(
                    this@AuthenticationActivity,
                    errString.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                startActivity(
                    Intent(this@AuthenticationActivity, MenuActivity::class.java),
                    ActivityOptions.makeCustomAnimation(
                        this@AuthenticationActivity,
                        R.anim.slide_forward,
                        R.anim.slide_backward
                    ).toBundle()
                )
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(
                    this@AuthenticationActivity,
                    "Authentication failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        biometricInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setDescription("Please authenticate to continue.")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
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
                                if (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS)
                                    biometricPrompt.authenticate(biometricInfo)
                                else {
                                    if (BuildConfig.DEBUG)
                                        startActivity(Intent(this, MenuActivity::class.java))
                                    else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "You can not login! Your device does not support fingerprint, iris, face, PIN, pattern, or password kind of authentication."
                                            )
                                        }
                                    }
                                }
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