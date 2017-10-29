package com.p_v.flexiblecalendarexample;

import com.p_v.flexiblecalendar.entity.Event;

/**
 * @author p-v
 */
public class CustomEvent implements Event {

    private int color;

    public CustomEvent(int color) {
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
}
