package com.example.eventmanagement.retrofit

import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.users.AuthUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventsApi {
    @POST("register")
    fun registerNewUser(@Body user: AuthUser): Call<Void>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(@Field("username") param1: String,
                  @Field("password") param2: String) : Call<Void>

    @GET("events")
    fun getEvents(): Call<EventModel>

    @GET("events")
    fun getEventsPages(@Query("page") page:Int): Call<EventModel>
}