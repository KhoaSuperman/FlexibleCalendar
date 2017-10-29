package com.p_v.flexiblecalendar.entity;

/**
 * @author p-v
 */
public interface Event {
    int getColor();
    boolean isMatchDate(int day, int month, int year);
    int getDay();
    int getMonth();
    int getYear();
}
