package com.tek.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val name: String? = null,
    val native: String? = null,
    val flag: String? = null,
    val numeric: String? = null,
    val capital: String? = null,
    val currency: String? = null,
    @SerialName("currency_symbol") val currencySymbol: String? = null,
    val region: String? = null,
    val subregion: String? = null,
    val idd: String? = null,
    val alpha2: String? = null,
    val alpha3: String? = null,
    val language: String? = null,
    @SerialName("language_code") val languageCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)