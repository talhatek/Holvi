package com.tek.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tek.network.data.BinApi
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.network.domain.mapper.BinInformationDtoToCardInformationMapper
import com.tek.network.interceptor.HeaderInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {


    factory {
        Retrofit.Builder().baseUrl("https://bin-ip-checker.p.rapidapi.com")
            .client(OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()
    }

    factory { get<Retrofit>().create(BinApi::class.java) }

    factoryOf(::GetCardInformationUseCase)
    factoryOf(::BinInformationDtoToCardInformationMapper)
}

