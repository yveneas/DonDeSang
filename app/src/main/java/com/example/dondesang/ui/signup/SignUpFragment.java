package com.example.dondesang.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentSignupBinding;
import com.example.dondesang.ui.account.informations.AccountInformationsFragment;
import com.example.dondesang.ui.connection.ConnectionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        listener();
        binding.inscriptionButton.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listener() {
        binding.inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.idTextField.getText().length() != 0) {
                    if(binding.passwordTextField.getText().length() != 0) {
                        if(binding.passwordTextField.getText().toString().equals(binding.passwordConfirmationTextField.getText().toString())) {
                            System.out.println("inscription");
                            mAuth.createUserWithEmailAndPassword(binding.idTextField.getText().toString(),
                                    binding.passwordTextField.getText().toString()).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        System.out.println("Inscription réussie");
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_activity_connection, new AccountInformationsFragment());
                                        fragmentTransaction.commit();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        binding.signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new ConnectionFragment());
                fragmentTransaction.commit();
            }
        });

        binding.passwordTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.idTextField.getText().length() != 0) {
                    if(binding.passwordTextField.getText().length() >= 6) {
                        if(binding.passwordTextField.getText().toString().equals(binding.passwordConfirmationTextField.getText().toString())) {
                            binding.inscriptionButton.setEnabled(true);
                        }
                    }
                }
                binding.inscriptionButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.passwordTextField.getText().length() < 6) {
                    binding.passwordTextField.setError("Le mot de passe doit contenir au moins 6 caractères");
                } else {
                    binding.inscriptionButton.setEnabled(true);
                }
            }
        });

        binding.passwordConfirmationTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.idTextField.getText().length() != 0) {
                    if(binding.passwordTextField.getText().length() >= 6) {
                        if(binding.passwordTextField.getText().toString().equals(binding.passwordConfirmationTextField.getText().toString())) {
                            binding.inscriptionButton.setEnabled(true);
                        }
                    }
                }
                binding.inscriptionButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!binding.passwordTextField.getText().toString().equals(binding.passwordConfirmationTextField.getText().toString())) {
                    binding.passwordConfirmationTextField.setError("Les mots de passe ne correspondent pas");
                } else {
                    binding.inscriptionButton.setEnabled(true);
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_activity_connection, new ConnectionFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }
}
