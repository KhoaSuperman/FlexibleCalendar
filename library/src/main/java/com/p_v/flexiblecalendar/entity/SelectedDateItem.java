package com.p_v.flexiblecalendar.entity;

/**
 * @author p-v
 */
public class SelectedDateItem {

    private int day;
    private int month;
    private int year;
    private int position;
    public boolean visible = true;

    public SelectedDateItem(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public SelectedDateItem(int year, int month, int day, int position) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.position = position;
    }

    @Override
    public SelectedDateItem clone() {
        return new SelectedDateItem(year, month, day, position);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectedDateItem that = (SelectedDateItem) o;

        if (day != that.day) return false;
        if (month != that.month) return false;
        return year == that.year;

    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }
}
