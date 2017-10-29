package com.p_v.flexiblecalendar.entity;

/**
 * @author p-v
 */
public class CalendarEvent implements Event {

    private int color;

    public CalendarEvent() {

    }

    public CalendarEvent(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isMatchDate(int day, int month, int year) {
        return false;
    }

    @Override
    public int getDay() {
        return 0;
    }

    @Override
    public int getMonth() {
        return 0;
    }

    @Override
    public int getYear() {
        return 0;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
