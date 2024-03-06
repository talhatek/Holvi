package com.tek.database.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ExportPasswordUseCase(private val db: FirebaseFirestore) {

    suspend operator fun invoke(pathId: String): QuerySnapshot =
        db.collection("port$pathId").get().await()

}
