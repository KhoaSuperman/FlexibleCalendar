package com.p_v.flexiblecalendar.entity;

import java.util.List;

/**
 * Created by hoang on 10/30/2017.
 */
public class CellData {
    public int month;
    public int year;
    public int day;
    public List<? extends Event> dayEvents;
    public int position;

    public CellData(int day, int month, int year, int position) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.position = position;
    }

    public void setDayEvents(List<? extends Event> dayEvents) {
        this.dayEvents = dayEvents;
    }
}
