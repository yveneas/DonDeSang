package com.example.dondesang.dao;

import com.example.dondesang.model.DonationCenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DonationCenterDAO {
    private FirebaseAuth auth;

    public DonationCenterDAO() {
        auth = FirebaseAuth.getInstance();
    }

    public List<DonationCenter> getAllDonationCenter() {
        List<DonationCenter> donationCenters = new ArrayList<>();
        if(auth != null) {
            DatabaseReference donationCenterRef = FirebaseDatabase.getInstance().getReference("donation_center");
            donationCenterRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        for(DataSnapshot donationCenter : snapshot.getChildren()) {
                            donationCenters.add(donationCenter.getValue(DonationCenter.class));
                        }
                    }
                }
            });
        }

        return donationCenters;
    }

    public void addDonationCenter(DonationCenter donationCenter) {
        if(auth != null) {
            DatabaseReference donationCenterRef = FirebaseDatabase.getInstance().getReference("donation_center");
            donationCenterRef.push().setValue(donationCenter);
        }
    }
}
