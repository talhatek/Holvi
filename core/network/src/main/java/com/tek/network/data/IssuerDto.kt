package com.tek.network.data

import kotlinx.serialization.Serializable

@Serializable
data class IssuerDto(
    val name: String? = null,
    val website: String? = null,
    val phone: String? = null
)