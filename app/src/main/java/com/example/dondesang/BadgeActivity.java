package com.example.dondesang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.model.User;
import com.example.dondesang.databinding.FragmentAccountMenuBinding;
import com.example.dondesang.ui.account.donations.BloodDonationsFragment;
import com.example.dondesang.ui.account.informations.AccountInformationsFragment;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BadgeActivity extends AppCompatActivity {
    private FragmentAccountMenuBinding binding;
    private FirebaseAuth mAuth;
    private User user;

    Button BackButton2;
    TextView nobadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        BackButton2 = findViewById(R.id.BackButton2);
        nobadge = findViewById(R.id.nobadge);

        BackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmenuFragment= new Intent(getApplicationContext(), MenuFragment.class);
                startActivity(intentmenuFragment);
            }
        });

    }
}