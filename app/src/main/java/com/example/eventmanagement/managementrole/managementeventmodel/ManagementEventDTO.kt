package com.example.eventmanagement.managementrole.managementeventmodel

data class ManagementEventDTO(
    val id: Long,
    val content: String,
    val endDay: String,
    val endTime: String,
    val location: String,
    val startDay: String,
    val startTime: String,
    val title: String,
    val enrollmentLink:String,
    val imageLink:String
)