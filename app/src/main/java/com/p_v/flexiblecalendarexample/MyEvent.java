package com.p_v.flexiblecalendarexample;

import com.p_v.flexiblecalendar.entity.Event;

/**
 * Created by hoang on 10/27/2017.
 */

public class MyEvent implements Event {

    int color;
    public final int day;
    public final int month;
    public final int year;
    int bgColor;

    public MyEvent(int color, int day, int month, int year) {
        this.color = color;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    @Override
    public int getColor() {
        return color;
    }
}
