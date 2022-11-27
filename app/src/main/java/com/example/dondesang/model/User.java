package com.example.dondesang.model;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class User {
    private String civility;
    private String id;
    private String email;
    private String name;
    private String lastName;
    private String birthDate;
    private String birthPlace;
    private Donation lastDonation;

    public User() {
    }

    public User(String civility, String email, String name, String lastName, String birthDate, String birthPlace) {
        this.civility = civility;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
    }

    public String getCivility() {
        return civility;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Donation getLastDonation() {
        return lastDonation;
    }

    public void setLastDonation(Donation lastDonation) {
        this.lastDonation = lastDonation;
    }

    public boolean isDonationPossible(DonationType type) {
        if(lastDonation == null) {
            return true;
        }
        Date date = lastDonation.getAppointment().convertToDate();
        if(date != null) {
            if(type == DonationType.BLOOD) {
                return date.getTime() + 56L * 24 * 60 * 60 * 1000 < new Date().getTime();
            } else if(type == DonationType.PLASMA) {
                return date.getTime() + 28L * 24 * 60 * 60 * 1000 < new Date().getTime();
            } else if(type == DonationType.PLAQUETTES) {
                return date.getTime() + 7 * 24 * 60 * 60 * 1000 < new Date().getTime();
            }
        }
        return false;
    }
}
