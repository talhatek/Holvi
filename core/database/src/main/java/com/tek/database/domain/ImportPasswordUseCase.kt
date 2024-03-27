package com.tek.database.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.tek.database.model.Password
import com.tek.util.CipherProvider

class ImportPasswordUseCase(
    private val db: FirebaseFirestore,
    private val cipherProvider: CipherProvider
) {
    operator fun invoke(pathId: String, index: String, data: Password): Task<Void> {
        return db.collection("port$pathId").document(index).set(
            data.copy(
                siteName = cipherProvider.encrypt(data.siteName, pathId),
                password = cipherProvider.encrypt(data.password, pathId),
                userName = cipherProvider.encrypt(data.userName, pathId),
            )
        )
    }
}
