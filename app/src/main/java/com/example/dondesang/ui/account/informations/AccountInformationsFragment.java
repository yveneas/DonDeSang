package com.example.dondesang.ui.account.informations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentAccountInformationsBinding;
import com.example.dondesang.User;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccountInformationsFragment extends Fragment {
    private FragmentAccountInformationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountInformationsBinding.inflate(inflater, container, false);
        listeners();
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add(0, "");
        spinnerArray.add("Monsieur");
        spinnerArray.add("Madame");

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.color_spinner_layout, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.civilityText);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null) {
                    binding.civilityText.setEnabled(false);
                    binding.lastnameText.setEnabled(false);
                    binding.nameText.setEnabled(false);
                    binding.birthDateText.setEnabled(false);
                    binding.birthPlaceText.setEnabled(false);
                    binding.nameText.setText(user.getName());
                    binding.lastnameText.setText(user.getLastName());
                    binding.civilityText.setSelection(spinnerArray.indexOf(user.getCivility()));
                    binding.birthDateText.setText(user.getBirthDate());
                    binding.birthPlaceText.setText(user.getBirthPlace());
                    binding.modifyButton.setText("Modifier");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });
        if(binding.nameText.getText().length() == 0) {
            binding.modifyButton.setText("Enregistrer");
            binding.civilityText.setEnabled(true);
            binding.lastnameText.setEnabled(true);
            binding.nameText.setEnabled(true);
            binding.birthDateText.setEnabled(true);
            binding.birthPlaceText.setEnabled(true);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listeners() {
        binding.birthDateText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : Math.min(mon, 12);
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900: Math.min(year, 2100);
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    binding.birthDateText.setText(current);
                    binding.birthDateText.setSelection(Math.min(sel, current.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                fragmentTransaction.commit();
            }
        });

        binding.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.modifyButton.getText().equals("Modifier")) {
                    binding.modifyButton.setText("Enregistrer");
                    binding.civilityText.setEnabled(true);
                    binding.lastnameText.setEnabled(true);
                    binding.nameText.setEnabled(true);
                    binding.birthDateText.setEnabled(true);
                    binding.birthPlaceText.setEnabled(true);
                } else if(binding.modifyButton.getText().equals("Enregistrer") &&
                        (binding.civilityText.getSelectedItem() != "" &&
                        binding.nameText.getText().length() != 0 && binding.lastnameText.getText().length() != 0 &&
                        binding.birthDateText.getText().length() != 0 && binding.birthPlaceText.getText().length() != 0)){
                    binding.modifyButton.setText("Modifier");
                    binding.civilityText.setEnabled(false);
                    binding.lastnameText.setEnabled(false);
                    binding.nameText.setEnabled(false);
                    binding.birthDateText.setEnabled(false);
                    binding.birthPlaceText.setEnabled(false);
                    User user = new User(binding.civilityText.getSelectedItem().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), binding.nameText.getText().toString(), binding.lastnameText.getText().toString(), binding.birthDateText.getText().toString(), binding.birthPlaceText.getText().toString());
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    userRef.setValue(user);
                } else {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new MenuFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }
}

