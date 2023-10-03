package com.example.eventmanagement.retrofit.testing

import com.example.eventmanagement.retrofit.EventsApi
import com.example.eventmanagement.retrofit.InterceptorRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestClient {
    companion object {

        private var BASE_URL = "https://eventmanagementplatform.onrender.com/"

        fun create() : TestApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(InterceptorRetrofit.client)
                .build()
            return retrofit.create(TestApi::class.java)

        }
    }
}