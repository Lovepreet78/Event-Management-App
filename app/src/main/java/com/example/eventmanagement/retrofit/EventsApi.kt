package com.example.eventmanagement.retrofit

import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.users.NewUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventsApi {
    @GET("events")
    fun getEvents(): Call<EventModel>

    @POST("register")
    fun registerNewUser(@Body user: NewUser): Call<Void>



}