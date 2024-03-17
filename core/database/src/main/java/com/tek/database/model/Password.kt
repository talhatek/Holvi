package com.tek.database.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Password(
    val id: Int,
    val siteName: String,
    val password: String,
    val userName: String,
)
