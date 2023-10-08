package com.example.eventmanagement.retrofit

import com.example.eventmanagement.admin.userEventModel.AdminUserDTO
import com.example.eventmanagement.admin.userEventModel.AdminUserModel
import com.example.eventmanagement.admin.userManager.AdminUsersModel.AdminAllUsersModel
import com.example.eventmanagement.eventmodel.EventDTO
import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.PostEventModel
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventsModel
import com.example.eventmanagement.users.AuthUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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



    @POST("/user/events/post")
    fun postEvent(@Body eventData: EventPostDTO):Call<Void>


    //admin
    @DELETE("/admin/events/delete/{id}")
    fun deleteEventByAdmin(@Path("id") eventId: Long): Call<Void>

    @POST("/admin/events/edit/{id}")
    fun editEventByAdmin(@Path("id") eventId:Long, @Body event:EventPostDTO):Call<Void>

    @GET("/admin/events")
    fun getAllEventsForAdmin(@Query("page") page:Int): Call<AdminUserModel>

    @POST("admin/updateUserRole/{id}")
    fun updateUserRole(@Path("id") userId:Long,@Body role:List<String>):Call<String>


    @GET("admin/users")
    fun getUsersForAdmin(@Query("page") page:Int): Call<AdminAllUsersModel>



    //management

    @GET("user/events")
    fun getManagementEvents(@Query("page") page:Int) : Call<ManagementEventsModel>

    @POST("user/events/edit/{ID}")
    fun editManagementEvent(@Path("ID") eventId:Long, @Body event:EventPostDTO):Call<Void>

    @DELETE("user/events/delete/{ID}")
    fun deleteManagementEvent(@Path("ID") id:Long):Call<Void>





}