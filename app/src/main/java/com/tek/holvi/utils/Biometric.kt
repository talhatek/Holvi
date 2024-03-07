package com.tek.holvi.utils

import android.app.ActivityOptions
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.tek.holvi.R
import com.tek.holvi.ui.activity.MenuActivity

val biometricInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Authenticate")
    .setDescription("Please authenticate to continue.")
    .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    .build()

val FragmentActivity.getBiometricPrompt: BiometricPrompt
    get() = BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            Toast.makeText(
                this@getBiometricPrompt,
                errString.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            startActivity(
                Intent(this@getBiometricPrompt, MenuActivity::class.java),
                ActivityOptions.makeCustomAnimation(
                    this@getBiometricPrompt,
                    R.anim.slide_forward,
                    R.anim.slide_backward
                ).toBundle()
            )
        }

        override fun onAuthenticationFailed() {
            Toast.makeText(
                this@getBiometricPrompt,
                "Authentication failed!",
                Toast.LENGTH_SHORT
            ).show()
        }
    })