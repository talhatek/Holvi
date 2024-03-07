package com.tek.database.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.tek.database.model.Password
import kotlinx.coroutines.tasks.await

class ImportPasswordUseCase(private val db: FirebaseFirestore) {
    suspend operator fun invoke(pathId: String, index: String, data: Password) {
        db.collection("port$pathId").document(index).set(data).await()
    }
}
