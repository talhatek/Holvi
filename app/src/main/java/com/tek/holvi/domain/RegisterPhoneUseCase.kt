package com.tek.holvi.domain

import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.tek.holvi.model.Key
import kotlinx.coroutines.tasks.await

class RegisterPhoneUseCase(private val firebaseFireStore: FirebaseFirestore) {

    suspend operator fun invoke(key: Key) {
        with(FirebaseInstallations.getInstance().id.await()) {
            firebaseFireStore.collection("unx").document(this + Build.MANUFACTURER).set(key)
        }
    }
}