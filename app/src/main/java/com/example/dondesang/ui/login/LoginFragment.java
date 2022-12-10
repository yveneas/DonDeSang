package com.example.dondesang.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.dao.UserDAO;
import com.example.dondesang.databinding.FragmentLoginBinding;
import com.example.dondesang.model.Donation;
import com.example.dondesang.model.User;
import com.example.dondesang.ui.account.informations.AccountInformationsFragment;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.example.dondesang.ui.connection.ConnectionFragment;
import com.example.dondesang.ui.infos.InfosFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        listener();
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listener() {
        binding.connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.idTextField.getText().length() != 0) {
                    if(binding.passwordTextField.getText().length() != 0) {
                        mAuth.signInWithEmailAndPassword(binding.idTextField.getText().toString(),
                                binding.passwordTextField.getText().toString()).addOnCompleteListener(getActivity(), task -> {
                            if(task.isSuccessful()) {
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
                                userRef.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        if(task1.getResult().exists()) {
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                                            fragmentTransaction.commit();
                                            ((UserActivity) getActivity()).setUser(task1.getResult().getValue(User.class));
                                            List<Donation> donations = new ArrayList<>();
                                            if(mAuth != null && mAuth.getCurrentUser() != null) {
                                                DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments");
                                                appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot type : snapshot.getChildren()) {
                                                            for (DataSnapshot date : type.getChildren()) {
                                                                for (DataSnapshot hour : date.getChildren()) {
                                                                    if (hour.getValue(User.class).getId().equals(mAuth.getCurrentUser().getUid())) {
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
                                                ((UserActivity) getActivity()).setDonations(donations);
                                            }
                                        } else {
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_activity_connection, new AccountInformationsFragment());
                                            fragmentTransaction.commit();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Veuillez entrer un identifiant", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new ConnectionFragment());
                fragmentTransaction.commit();
            }
        });

        binding.textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fonctionnalité en cours de développement", Toast.LENGTH_SHORT).show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new ConnectionFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }
}
