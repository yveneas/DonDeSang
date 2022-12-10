package com.example.dondesang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dondesang.databinding.FragmentAccountMenuBinding;
import com.example.dondesang.model.User;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;

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