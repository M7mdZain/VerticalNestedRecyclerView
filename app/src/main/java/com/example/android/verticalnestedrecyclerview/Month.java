package com.example.android.verticalnestedrecyclerview;

/*
 * By: M7md.zain@gamil.com
 * 04.July.2021
 * */

public class Month {

    String name;
    int dayCount;
    int firstVisibleDay = -1;
    int lastVisibleDay = -1;

    public Month(String name, int dayCount) {
        this.name = name;
        this.dayCount = dayCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getFirstVisibleDay() {
        return firstVisibleDay;
    }

    public void setFirstVisibleDay(int firstVisibleDay) {
        this.firstVisibleDay = firstVisibleDay;
    }

    public int getLastVisibleDay() {
        return lastVisibleDay;
    }

    public void setLastVisibleDay(int lastVisibleDay) {
        this.lastVisibleDay = lastVisibleDay;
    }
}
