package com.example.eventmanagement.admin.userEventModel

data class AdminUserDTO(
    val id: Long,
    val content: String,
    val endDay: String,
    val endTime: String,
    val location: String,
    val postedAt: String,
    val postedBy: String,
    val startDay: String,
    val startTime: String,
    val title: String,
    val enrollmentLink:String,
    val imageLink:String

)