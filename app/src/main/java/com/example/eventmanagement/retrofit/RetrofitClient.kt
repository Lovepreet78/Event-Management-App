package com.example.eventmanagement.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {

        private var BASE_URL = "https://eventmanagementplatform.onrender.com/"

        fun create() : EventsApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(EventsApi::class.java)

        }
    }
}