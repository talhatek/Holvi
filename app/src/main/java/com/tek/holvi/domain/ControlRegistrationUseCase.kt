package com.tek.holvi.domain

import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.tek.holvi.model.Key
import kotlinx.coroutines.tasks.await

class ControlRegistrationUseCase(private val firebaseFireStore: FirebaseFirestore) {

    suspend operator fun invoke(): Key? {
        FirebaseInstallations.getInstance().id.await().also { id ->
            return firebaseFireStore.collection("unx")
                .document(id + Build.MANUFACTURER).get().await()
                .toObject(Key::class.java)
        }
    }
}