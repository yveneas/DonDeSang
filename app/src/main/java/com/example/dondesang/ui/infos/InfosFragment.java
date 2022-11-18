package com.example.dondesang.ui.infos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dondesang.databinding.FragmentInfosBinding;

public class InfosFragment extends Fragment {

    private FragmentInfosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InfosViewModel notificationsViewModel =
                new ViewModelProvider(this).get(InfosViewModel.class);

        binding = FragmentInfosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textInfos;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}