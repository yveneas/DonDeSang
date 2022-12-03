package com.example.dondesang.ui.account.donations;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.dao.UserDAO;
import com.example.dondesang.databinding.FragmentDonationsListBinding;
import com.example.dondesang.model.Donation;
import com.example.dondesang.model.DonationType;
import com.example.dondesang.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonationsInfosFragment extends Fragment {

    private FragmentDonationsListBinding binding;
    private User user;
    private List<Donation> donations;
    private UserDAO userDAO;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDonationsListBinding.inflate(inflater, container, false);
        donations = new ArrayList<>();

        userDAO = new UserDAO();
        UserActivity activity = (UserActivity) getActivity();
        user = activity.getUser();
        donations = activity.getDonations();

        listeners();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onStart() {
        super.onStart();
        String typeDonation = getArguments().getString("type");
        if (typeDonation != null) {
            if (typeDonation.equals("blood")) {
                binding.donationInfosTitle.setText("Donations de sang");
            } else if (typeDonation.equals("plasma")) {
                binding.donationInfosTitle.setText("Donations de plasma");
            } else if (typeDonation.equals("plaquettes")) {
                binding.donationInfosTitle.setText("Donations de plaquettes");
            }
        }
        for(Donation donation : donations) {
            if(donation.getType().equals(typeDonation)) {
                View view = getLayoutInflater().inflate(R.layout.single_donation, null);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextView text = view.findViewById(R.id.donationTypeText);
                if(donation.getType().equals("blood")) {
                    text.setText("Sang");
                    Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.blood);
                    assert img != null;
                    img.setBounds(0, 0, 68, 80);
                    text.setCompoundDrawables(img, null, null, null);
                } else if(donation.getType().equals("plasma")) {
                    text.setText("Plasma");
                    Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.plasma);
                    assert img != null;
                    img.setBounds(0, 0, 68, 80);
                    text.setCompoundDrawables(img, null, null, null);
                } else if(donation.getType().equals("plaquettes")) {
                    text.setText("Plaquettes");
                    Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.plaquette);
                    assert img != null;
                    img.setBounds(0, 0, 68, 80);
                    text.setCompoundDrawables(img, null, null, null);
                }
                text = view.findViewById(R.id.donationDateText);
                String date = "";
                try {
                    Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(donation.getDate().replace("-", "/"));
                    assert date1 != null;
                    date = new SimpleDateFormat("dd/MM/yyyy").format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                text.setText(date);
                binding.donationsLayout.addView(view);
            }

        }
    }

    public void listeners() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new BloodDonationsFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }
}
