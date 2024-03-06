package com.tek.database.domain

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ExportPasswordUseCase(
    private val db: FirebaseFirestore
) {

    suspend operator fun invoke(pathId: String): ExportResult {
        db.collection("port$pathId").get().await().let { querySnapshot ->
            if (querySnapshot.documents.isEmpty()) {
                return ExportResult.Error
            } else {
                return ExportResult.Success(querySnapshot.documents.mapNotNull { it.data })
            }

        }
    }
}

sealed class ExportResult {
    data class Success(val data: List<Map<String, Any>>) : ExportResult()
    data object Error : ExportResult()
}
