package com.example.eventmanagement.eventmodel

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.function.LongFunction

class LocalTimeConverter {
    companion object{
        fun convertStringTOLocalTIme(hour:Int,minutes:Int,second:Int,millis: Int,nano:Int):LocalTime{

            val  localTime = LocalTime.of(hour,minutes,second,nano)
            val formatter=DateTimeFormatter.ofPattern("HH:mm:ss")
            localTime.format(formatter)
            return localTime;
        }

        fun intToLocalDate(year: Int, month: Int, day: Int): LocalDate {
            return LocalDate.of(year, month, day)
        }
    }
}