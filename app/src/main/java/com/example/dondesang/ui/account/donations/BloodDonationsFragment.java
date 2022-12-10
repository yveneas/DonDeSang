package com.example.dondesang.ui.account.donations;

import android.annotation.SuppressLint;
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

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.databinding.FragmentBloodDonationsBinding;
import com.example.dondesang.model.DonationType;
import com.example.dondesang.model.User;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class BloodDonationsFragment extends Fragment {

    private FragmentBloodDonationsBinding binding;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBloodDonationsBinding.inflate(inflater, container, false);
        UserActivity activity = (UserActivity) getActivity();
        user = activity.getUser();
        listeners();
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null && user.getLastDonation() != null) {
                    if(!user.isDonationPossible(DonationType.BLOOD)) {
                        binding.bloodAppointmentButton.setEnabled(false);
                        binding.bloodAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
                    }
                    if(!user.isDonationPossible(DonationType.PLASMA)) {
                        binding.plasmaAppointmentButton.setEnabled(false);
                        binding.plasmaAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
                    }
                    if(!user.isDonationPossible(DonationType.PLAQUETTES)) {
                        binding.plaquetteAppointmentButton.setEnabled(false);
                        binding.plaquetteAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
                    }
                }
                binding.bloodNextDonation.setText("Prochain  don :\n" + new SimpleDateFormat("dd/MM/yyyy").format(user.getNextDonationDate(DonationType.BLOOD)));
                binding.plasmaNextDonation.setText("Prochain  don :\n" + new SimpleDateFormat("dd/MM/yyyy").format(user.getNextDonationDate(DonationType.PLASMA)));
                binding.plaquetteNextDonation.setText("Prochain  don :\n" + new SimpleDateFormat("dd/MM/yyyy").format(user.getNextDonationDate(DonationType.PLAQUETTES)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

        UserActivity activity = (UserActivity) getActivity();
        user = activity.getUser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listeners() {
        binding.donationBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                fragmentTransaction.commit();
            }
        });
        if(user.isDonationPossible(DonationType.BLOOD)) {
            binding.bloodAppointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    /*Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                    intent.putExtra("type", "sang");
                    startActivity(intent);*/
                }
            });
        } else {
            binding.bloodAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
        }

        if(user.isDonationPossible(DonationType.PLASMA)) {
            binding.plasmaAppointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    /*Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                    intent.putExtra("type", "sang");
                    startActivity(intent);*/
                }
            });
        } else {
            binding.plasmaAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
        }

        if(user.isDonationPossible(DonationType.PLAQUETTES)) {
            binding.plaquetteAppointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    /*Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                    intent.putExtra("type", "sang");
                    startActivity(intent);*/
                }
            });
        } else {
            binding.plaquetteAppointmentButton.setBackground(getResources().getDrawable(R.drawable.back_button_background));
        }

        binding.infosButonBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "blood");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DonationsInfosFragment donationsInfosFragment = new DonationsInfosFragment();
                donationsInfosFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_activity_connection, donationsInfosFragment);
                fragmentTransaction.commit();
            }
        });

        binding.infosButtonPlasma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "plasma");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DonationsInfosFragment donationsInfosFragment = new DonationsInfosFragment();
                donationsInfosFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_activity_connection, donationsInfosFragment);
                fragmentTransaction.commit();
            }
        });

        binding.infosButtonPlaquette.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "plaquettes");
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        DonationsInfosFragment donationsInfosFragment = new DonationsInfosFragment();
                        donationsInfosFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_activity_connection, donationsInfosFragment);
                        fragmentTransaction.commit();
                    }
                });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }
}
