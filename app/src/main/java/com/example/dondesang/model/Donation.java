package com.example.dondesang.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Donation {
    private String date;
    private String type;

    public Donation() {
    }

    public Donation(String date, String type) {
        this.date = date;
        this.type = type;
    }

    public Donation(String date, DonationType type) {
        this.date = date;
        this.type = type.getName();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
