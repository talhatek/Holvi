package com.tek.holvi.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.tek.holvi.BuildConfig
import com.tek.holvi.ui.AuthenticationMainScreen
import com.tek.holvi.utils.biometricInfo
import com.tek.holvi.utils.getBiometricPrompt
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import kotlinx.coroutines.launch

class AuthenticationActivity : FragmentActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricManager: BiometricManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        biometricManager = BiometricManager.from(this)
        biometricPrompt = this.getBiometricPrompt
        setContent {
            HolviTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                HolviScaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    content = {
                        AuthenticationMainScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = it.calculateTopPadding()),
                            onClick = {
                                if (BuildConfig.DEBUG) {
                                    startActivity(Intent(this, MenuActivity::class.java))

                                } else {
                                    if (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
                                        biometricPrompt.authenticate(biometricInfo)

                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "You can not login! Your device does not support fingerprint, iris, face, PIN, pattern, or password kind of authentication."
                                            )
                                        }
                                    }
                                }
                            },
                            onMessageDeliver = {
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
