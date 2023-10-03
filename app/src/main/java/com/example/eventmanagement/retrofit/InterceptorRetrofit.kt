package com.example.eventmanagement.retrofit

import com.example.eventmanagement.constants.Cookie
import okhttp3.OkHttpClient

object InterceptorRetrofit {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithCookie = originalRequest.newBuilder()
                .addHeader("Cookie", Cookie.cookie)
                .addHeader("Content-Type","application/json")
                .build()
            chain.proceed(requestWithCookie)
        }
        .build()
}