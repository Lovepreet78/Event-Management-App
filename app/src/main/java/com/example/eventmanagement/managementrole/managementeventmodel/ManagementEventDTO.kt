package com.example.eventmanagement.managementrole.managementeventmodel

data class ManagementEventDTO(
    val ID: Long,
    val content: String,
    val endDay: String,
    val endTime: String,
    val location: String,
    val startDay: String,
    val startTime: String,
    val title: String
)