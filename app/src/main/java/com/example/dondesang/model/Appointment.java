package com.example.dondesang.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Appointment {

    private String date;
    private Hour hour;
    private User user;

    public Appointment() {
    }

    public Appointment(String date, Hour hour, User user) {
        this.date = date.replace("/", "-");
        this.hour = hour;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SuppressLint("SimpleDateFormat")
    public Date convertToDate() {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
