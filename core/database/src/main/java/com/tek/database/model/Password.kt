package com.tek.database.model

import androidx.compose.runtime.Immutable


@Immutable
data class Password(
    val id: Int,
    val siteName: String,
    val password: String,
    val userName: String,
)
