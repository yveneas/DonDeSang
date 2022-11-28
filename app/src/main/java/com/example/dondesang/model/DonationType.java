package com.example.dondesang.model;

public enum DonationType {
    BLOOD("Don de sang"),
    PLASMA("Don de plasma"),
    PLAQUETTES("Don de plaquettes");

    private String name;

    DonationType(){

    }

    DonationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
