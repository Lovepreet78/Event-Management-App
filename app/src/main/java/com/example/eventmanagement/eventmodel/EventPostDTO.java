package com.example.eventmanagement.eventmodel;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;

public final class EventPostDTO {
    private final Long ID;
    private final String title;
    private final String content;
    private final String location;
    private final String startDay;
    private final String endDay;
    private final String startTime;
    private final String endTime;

//    public EventPostDTO(Long ID, String title, String content, String location, LocalDate startDay,
//                 LocalDate endDay,
//                 LocalTime startTime, LocalTime endTime)
    public EventPostDTO(Long ID, String title, String content, String location, String startDay,
                 String endDay,
                 String startTime, String endTime)
    {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDay = startDay.toString();
        this.endDay = endDay.toString();
//        this.startTime = startTime.format( DateTimeFormatter.ofPattern("HH:mm:ss"));
//        this.endTime = endTime.format( DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long ID() {
        return ID;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public String location() {
        return location;
    }

    public String startDay() {
        return startDay;
    }

    public String endDay() {
        return endDay;
    }

    public String startTime() {
        return startTime;
    }

    public String endTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        EventPostDTO that = (EventPostDTO) obj;
        return Objects.equals(this.ID, that.ID) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.content, that.content) &&
                Objects.equals(this.location, that.location) &&
                Objects.equals(this.startDay, that.startDay) &&
                Objects.equals(this.endDay, that.endDay) &&
                Objects.equals(this.startTime, that.startTime) &&
                Objects.equals(this.endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, title, content, location, startDay, endDay, startTime, endTime);
    }

    @Override
    public String toString() {
        return "EventPostDTO[" +
                "ID=" + ID + ", " +
                "title=" + title + ", " +
                "content=" + content + ", " +
                "location=" + location + ", " +
                "startDay=" + startDay + ", " +
                "endDay=" + endDay + ", " +
                "startTime=" + startTime + ", " +
                "endTime=" + endTime + ']';
    }
}
