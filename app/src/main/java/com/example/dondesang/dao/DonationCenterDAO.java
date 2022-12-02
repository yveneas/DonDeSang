package com.example.dondesang.dao;

import androidx.annotation.NonNull;

import com.example.dondesang.model.Donation;
import com.example.dondesang.model.DonationCenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            donationCenterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot donationCenter : snapshot.getChildren()) {
                        donationCenters.add(donationCenter.getValue(DonationCenter.class));
                    }
                    System.out.println("size " + donationCenters.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
