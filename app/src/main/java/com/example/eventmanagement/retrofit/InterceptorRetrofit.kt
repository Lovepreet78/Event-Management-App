package com.example.eventmanagement.retrofit

import com.example.eventmanagement.constants.Cookie
import okhttp3.OkHttpClient

object InterceptorRetrofit {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            // Add your cookie to the request headers
            val requestWithCookie = originalRequest.newBuilder()
                .addHeader("Cookie", Cookie.cookie) // Replace with your actual cookie key and value
                .build()
            chain.proceed(requestWithCookie)
        }
        .build()
}