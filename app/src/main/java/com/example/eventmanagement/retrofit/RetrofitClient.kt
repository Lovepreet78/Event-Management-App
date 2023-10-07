package com.example.eventmanagement.retrofit

import com.example.eventmanagement.constants.Constants
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitClient {
    companion object {

        private var BASE_URL = "https://eventmanagementplatform.onrender.com/"
//        private var BASE_URL = "http://192.168.164.72/"


        fun create() : EventsApi {
            val gson =  GsonBuilder()
                .setLenient()
                .create();

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(InterceptorRetrofit.client)
                .build()
            return retrofit.create(EventsApi::class.java)

        }
    }
}