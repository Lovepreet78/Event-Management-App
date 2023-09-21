package com.example.eventmanagement.eventmodel

data class EventDTO(
    val ID: Long,
    val content: String,
    val endDay: String,
    val endTime: String,
    val location: String,
    val startDay: String,
    val startTime: String,
    val title: String
)