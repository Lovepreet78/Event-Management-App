package com.example.eventmanagement.retrofit.testing

import com.example.eventmanagement.eventmodel.PostEventModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TestApi {
    @POST("/user/events/post")
    fun testPostEvent(@Body eventData: PostEventModel): Call<String>

}