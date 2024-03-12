package com.tek.network.interceptor

import com.tek.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("X-RapidAPI-Key", BuildConfig.BIN_CHECKER_API_KEY)
                .build()
        )
    }
}