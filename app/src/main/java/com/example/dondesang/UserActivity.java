package com.example.dondesang;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.dao.UserDAO;
import com.example.dondesang.model.Donation;
import com.example.dondesang.model.User;
import com.example.dondesang.ui.account.informations.AccountInformationsFragment;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.example.dondesang.ui.connection.ConnectionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private User user;
    private List<Donation> donations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            userRef.child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if(snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                        UserDAO userDAO = new UserDAO();
                        donations = userDAO.getUserAllDonations();

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                        fragmentTransaction.commit();
                    }
                }
            });


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_activity_connection, new AccountInformationsFragment());
            fragmentTransaction.commit();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_activity_connection, new ConnectionFragment());
            fragmentTransaction.commit();
        }
        listeners();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        return true;
    }

    public void listeners() {
        View view = findViewById(R.id.logoImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        view = findViewById(R.id.accountButton);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(UserActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.account_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        if (item.getItemId() == R.id.logout) {
                            mAuth.signOut();
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}
