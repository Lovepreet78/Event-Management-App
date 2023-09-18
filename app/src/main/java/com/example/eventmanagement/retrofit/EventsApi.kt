package com.example.eventmanagement.retrofit

import com.example.eventmanagement.eventmodel.EventModel
import retrofit2.Call
import retrofit2.http.GET

interface EventsApi {
    @GET("events")
    fun getEvents(): Call<EventModel>


}