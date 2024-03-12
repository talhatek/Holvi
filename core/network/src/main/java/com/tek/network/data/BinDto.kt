package com.tek.network.data

import kotlinx.serialization.Serializable

@Serializable
data class BinDto(
    val valid: Boolean? = null,
    val number: Int? = null,
    val length: Int? = null,
    val scheme: String? = null,
    val brand: String? = null,
    val type: String? = null,
    val level: String? = null,
    val currency: String? = null,
    val issuer: IssuerDto? = IssuerDto(),
    val country: CountryDto? = CountryDto()
)