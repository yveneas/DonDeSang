package com.example.dondesang.dao;

import androidx.annotation.NonNull;

import com.example.dondesang.model.Donation;
import com.example.dondesang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private FirebaseAuth auth;

    public UserDAO() {
        auth = FirebaseAuth.getInstance();
    }

    public List<Donation> getUserAllDonations() {
        List<Donation> donations = new ArrayList<>();
        if(auth != null && auth.getCurrentUser() != null) {
            DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments");
            appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot type : snapshot.getChildren()) {
                        for(DataSnapshot date : type.getChildren()) {
                            for(DataSnapshot hour : date.getChildren()) {
                                if(hour.getValue(User.class).getId().equals(auth.getCurrentUser().getUid())) {
                                    System.out.println("Donation found");
                                    donations.add(new Donation(date.getKey(), type.getKey()));
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return donations;
    }
}
