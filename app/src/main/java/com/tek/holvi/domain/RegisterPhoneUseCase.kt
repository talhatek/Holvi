package com.tek.holvi.domain

import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.tek.holvi.model.Key
import kotlinx.coroutines.tasks.await
import java.util.UUID

class RegisterPhoneUseCase(private val firebaseFireStore: FirebaseFirestore) {

    suspend operator fun invoke() {
        with(FirebaseInstallations.getInstance().id.await()) {
            Key(id = UUID.randomUUID().toString()).also { key ->
                firebaseFireStore.collection("unx").document(this + Build.MANUFACTURER).set(key)
            }
        }
    }
}