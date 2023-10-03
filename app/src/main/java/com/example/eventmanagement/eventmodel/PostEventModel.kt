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


){
    override fun toString(): String {
        return """{"ID":$ID, "title":"$title", "content":"$content", "location":"$location", "startDay":"$startDay", "endDay":"$endDay", "startTime":"$startTime", "endTime":"$endTime"}"""
        
    }
}