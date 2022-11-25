package com.example.dondesang.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentSignupBinding;
import com.example.dondesang.ui.account.informations.AccountInformationsFragment;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.example.dondesang.ui.connection.ConnectionFragment;
import com.example.dondesang.ui.infos.InfosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SignUpViewModel signUpViewModel =
                new ViewModelProvider(this).get(SignUpViewModel.class);

        binding = FragmentSignupBinding.inflate(inflater, container, false);
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
        binding.inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.idTextField.getText().length() != 0) {
                    if(binding.passwordTextField.getText().length() != 0) {
                        if(binding.passwordTextField.getText().toString().equals(binding.passwordConfirmationTextField.getText().toString())) {
                            mAuth.createUserWithEmailAndPassword(binding.idTextField.getText().toString(),
                                    binding.passwordTextField.getText().toString()).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
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
