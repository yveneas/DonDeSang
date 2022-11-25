package com.example.dondesang.ui.account.donations;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.AppointmentActivity;
import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentBloodDonationsBinding;
import com.example.dondesang.ui.account.menu.MenuFragment;

public class BloodDonationsFragment extends Fragment {
    private FragmentBloodDonationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBloodDonationsBinding.inflate(inflater, container, false);
        listeners();
        return binding.getRoot();
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

        binding.bloodAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                intent.putExtra("type", "sang");
                startActivity(intent);
            }
        });

        binding.plasmaAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                intent.putExtra("type", "plasma");
                startActivity(intent);
            }
        });

        binding.plaquetteAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                intent.putExtra("type", "plaquettes");
                startActivity(intent);
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
