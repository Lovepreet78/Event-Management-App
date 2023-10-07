package com.example.eventmanagement.managementrole.managementeventmodel

data class ManagementEventsModel(
    val content: List<ManagementEventDTO>,
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