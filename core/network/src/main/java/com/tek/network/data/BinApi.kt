package com.tek.network.data

import retrofit2.http.POST
import retrofit2.http.Query


interface BinApi {

    @POST(".")
    suspend fun binInformation(@Query("bin") bin: String): BinInformationDto
}