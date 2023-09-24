package com.example.eventmanagement.eventmodel

import java.time.LocalDate
import java.time.LocalTime

data class PostEventModel(

    val ID: Long,
    val title: String,
    val content: String,
    val location: String,
    val startDay: String,
    val endDay: String,
    val startTime: LocalTime,
    val endTime: LocalTime


)
