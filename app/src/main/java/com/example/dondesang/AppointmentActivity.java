package com.example.dondesang;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.dao.UserDAO;
import com.example.dondesang.model.User;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentActivity extends AppCompatActivity {

    private String centerId;
    private User user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        centerId = getIntent().getStringExtra("centerId");

    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_appointment);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            userRef.child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                    }
                }
            });
        }
    }

    public String getCenterId() {
        return centerId;
    }
}
