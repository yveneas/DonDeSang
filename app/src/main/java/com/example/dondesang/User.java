package com.example.dondesang;

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

    private String generateRandomId() {
        StringBuffer sb = new StringBuffer();
        sb.append(UUID.randomUUID().toString().replace("-", ""));
        sb.append(UUID.randomUUID().toString().replace("-", ""));
        sb.append(UUID.randomUUID().toString().replace("-", ""));
        sb.append(UUID.randomUUID().toString().replace("-", ""));
        int random = new Random().nextInt(sb.length() - 10);
        return sb.toString().substring(random, random + 10);
    }
}
