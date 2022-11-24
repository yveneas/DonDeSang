package com.example.dondesang.ui.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentAppointmentMainBinding;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {
    private FragmentAppointmentMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAppointmentMainBinding.inflate(inflater, container, false);
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Don de sang");
        spinnerArray.add("Don de plasma");
        spinnerArray.add("Don de plaquettes");

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.color_spinner_layout, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.donationsSpinner);
        spinner.setAdapter(adapter);
        listeners();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listeners() {
        binding.donationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                binding.donationsSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
