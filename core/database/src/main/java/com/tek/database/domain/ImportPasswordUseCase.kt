package com.tek.database.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.tek.database.model.Password
import com.tek.util.CipherProvider
import kotlinx.coroutines.tasks.await

class ImportPasswordUseCase(
    private val db: FirebaseFirestore,
    private val cipherProvider: CipherProvider
) {
    suspend operator fun invoke(pathId: String, index: String, data: Password) {
        db.collection("port$pathId").document(index).set(
            data.copy(
                siteName = cipherProvider.encrypt(data.siteName, pathId),
                password = cipherProvider.encrypt(data.password, pathId),
                userName = cipherProvider.encrypt(data.userName, pathId),
            )
        ).await()
    }
}
