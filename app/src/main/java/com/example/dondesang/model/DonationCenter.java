package com.example.dondesang.model;

import java.util.List;

public class DonationCenter {

    private String name;
    private String address;
    private List<DonationType> donationTypes;

    public DonationCenter() {    }

    public DonationCenter(String name, String address, List<DonationType> donationTypes) {
        this.name = name;
        this.address = address;
        this.donationTypes = donationTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DonationType> getDonationTypes() {
        return donationTypes;
    }

    public void setDonationTypes(List<DonationType> donationTypes) {
        this.donationTypes = donationTypes;
    }
}
