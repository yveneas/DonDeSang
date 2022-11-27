package com.example.dondesang.model;

public class Donation {
    private Appointment appointment;
    private DonationType type;

    public Donation() {
    }

    public Donation(Appointment appointment, DonationType type) {
        this.appointment = appointment;
        this.type = type;
    }

    public Donation(Appointment appointment, String type) {
        this.appointment = appointment;
        this.type = DonationType.valueOf(type);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public DonationType getType() {
        return type;
    }

    public void setType(DonationType type) {
        this.type = type;
    }
}
