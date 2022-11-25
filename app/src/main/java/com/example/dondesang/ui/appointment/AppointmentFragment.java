package com.example.dondesang.ui.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
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
        spinner.setSelection(1);
        binding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
        listeners();
        Bundle bundle = requireActivity().getIntent().getExtras();
        if(bundle != null) {
            String type = bundle.getString("type");
            if(type.equals("sang")) {
                spinner.setSelection(0);
            } else if(type.equals("plasma")) {
                spinner.setSelection(1);
            } else if(type.equals("plaquettes")) {
                spinner.setSelection(2);
            }
        }
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
                if(item.equals("Don de sang")) {
                    binding.spinnerImage.setImageResource(R.drawable.blood);
                } else if(item.equals("Don de plasma")) {
                    binding.spinnerImage.setImageResource(R.drawable.plasma);
                } else if(item.equals("Don de plaquettes")) {
                    binding.spinnerImage.setImageResource(R.drawable.plaquette);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.dateSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_appointment_hour_picker);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        binding.appointmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
}
