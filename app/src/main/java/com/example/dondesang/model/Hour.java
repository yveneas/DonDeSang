package com.example.dondesang.model;

import androidx.annotation.NonNull;

public class Hour {
    private int hour;
    private int minute;

    public Hour() {
    }

    public Hour(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Hour(String hour) {
        String[] parts = hour.split(":");
        this.hour = Integer.parseInt(parts[0]);
        this.minute = Integer.parseInt(parts[1]);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @NonNull
    @Override
    public String toString() {
        if(minute < 10) {
            return hour + ":0" + minute;
        } else {
            return hour + ":" + minute;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Hour) {
            Hour hour = (Hour) obj;
            return hour.getHour() == this.hour && hour.getMinute() == this.minute;
        }
        return false;
    }
}
