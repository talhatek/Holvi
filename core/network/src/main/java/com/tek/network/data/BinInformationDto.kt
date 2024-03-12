package com.tek.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BinInformationDto(
    val success: Boolean,
    val code: Int,
    @SerialName("BIN") val bin: BinDto? = BinDto()
)

