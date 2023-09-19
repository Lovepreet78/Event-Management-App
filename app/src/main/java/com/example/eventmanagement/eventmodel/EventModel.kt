package com.example.eventmanagement.eventmodel

class EventModel(
//    val content: String,
//    val endDay: String,
//    val endTime: String,
//    val id: Long,
//    val location: String,
//    val startDay: String,
//    val startTime: String,
//    val title: String
    val content: List<EventDTO>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX,
    val totalElements: Int,
    val totalPages: Int
)