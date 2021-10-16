package com.example.holvi.ui.authenticationActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.material.Scaffold
import androidx.fragment.app.FragmentActivity
import com.example.holvi.BuildConfig
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.authenticationActivity.composable.AuthenticationMainScreen
import com.example.holvi.ui.menuActivity.MenuActivity

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
                Toast.makeText(this@AuthenticationActivity, "Succeed", Toast.LENGTH_SHORT).show()
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
                Scaffold(
                    content = {
                        AuthenticationMainScreen {
                            if (biometricManager.canAuthenticate(DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS)
                                biometricPrompt.authenticate(biometricInfo)
                            else {
                                if (BuildConfig.DEBUG)
                                    startActivity(Intent(this, MenuActivity::class.java))
                                else
                                    Toast.makeText(
                                        this@AuthenticationActivity,
                                        "You can not login!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

