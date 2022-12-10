package com.example.dondesang.ui.appointment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dondesang.AppointmentActivity;
import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentAppointmentMainBinding;
import com.example.dondesang.model.Appointment;
import com.example.dondesang.model.Donation;
import com.example.dondesang.model.DonationType;
import com.example.dondesang.model.Hour;
import com.example.dondesang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {
    private FragmentAppointmentMainBinding binding;
    private String selectedDate;
    private List<Hour> takenHours;
    private String centerId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAppointmentMainBinding.inflate(inflater, container, false);
        takenHours = new ArrayList<>();
        AppointmentActivity activity = (AppointmentActivity) getActivity();
        centerId = ((AppointmentActivity) getActivity()).getCenterId();

        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Don de sang");
        spinnerArray.add("Don de plasma");
        spinnerArray.add("Don de plaquettes");

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.color_spinner_layout, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.donationsSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(selectedDate == null) {
            selectedDate = sdf.format(binding.calendarView.getDate());
            getAllTakenAppointments(selectedDate, getAllAppointments());
        }

        binding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            getAllTakenAppointments(selectedDate, getAllAppointments());
        });

        listeners();
        Bundle bundle = requireActivity().getIntent().getExtras();
        if(bundle != null) {
            String type = bundle.getString("type");
            if(type != null && type.equals("sang")) {
                spinner.setSelection(0);
            } else if(type != null && type.equals("plasma")) {
                spinner.setSelection(1);
            } else if(type != null && type.equals("plaquettes")) {
                spinner.setSelection(2);
            } else {
                spinner.setSelection(0);
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
                getAllTakenAppointments(selectedDate, getAllAppointments());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.dateSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Hour> allHours = getAllAppointments();
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_appointment_hour_picker);
                TableLayout tableLayout = dialog.findViewById(R.id.hourPickerTableLayout);
                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tableRow.setGravity(Gravity.CENTER);
                int i = 0;
                for(Hour hour : allHours) {
                    if(i > 2) {
                        tableLayout.addView(tableRow);
                        tableRow = new TableRow(getActivity());
                        tableRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        tableRow.setGravity(Gravity.CENTER);
                        i = 0;
                    }
                    Button button = new Button(getActivity());
                    button.setText(hour.toString());

                    if(!takenHours.contains(hour)) {
                        button.setBackground(getActivity().getDrawable(R.drawable.button_background));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                userRef.child(mAuth.getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user = snapshot.getValue(User.class);
                                                DonationType type = getSpinnerValue();
                                                String donationType = "";
                                                if(type.getName().equals("Don de sang")) {
                                                    donationType = "blood";
                                                } else if(type.getName().equals("Don de plasma")) {
                                                    donationType = "plasma";
                                                } else if(type.getName().equals("Don de plaquettes")) {
                                                    donationType = "plaquettes";
                                                }
                                                Appointment appointment = new Appointment(selectedDate, hour, user);
                                                DatabaseReference appointmentRef = FirebaseDatabase.getInstance()
                                                        .getReference("donation_center");
                                                appointmentRef.child(((AppointmentActivity)getActivity()).getCenterId()).child("appointments").child(donationType).child(selectedDate.replace("/", "-"))
                                                        .child(hour.toString()).setValue(appointment.getUser());
                                                Toast.makeText(getActivity(),
                                                        "Rendez-vous pris avec succès", Toast.LENGTH_LONG).show();
                                                user.setLastDonation(new Donation(appointment.getDate(), type));
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                                userRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
                                                dialog.dismiss();
                                                takenHours.add(hour);
                                                getActivity().finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });
                    } else {
                        button.setBackground(getActivity().getDrawable(R.drawable.back_button_background));
                    }

                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT ,
                            TableRow.LayoutParams.WRAP_CONTENT );
                    layoutParams.setMargins( 0, 0, 10, 0 ) ;
                    button.setLayoutParams(layoutParams);
                    i++;
                    tableRow.addView(button);
                }
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

    public List<Hour> getAllAppointments() {
        List<Hour> hours = new ArrayList<>();
        for(int i = 8; i < 20; i++) {
            for(int j = 0; j < 60; j += 20) {
                hours.add(new Hour(i, j));
            }
        }
        return hours;
    }

    public void getAllTakenAppointments(String date, List<Hour> allHours) {
        takenHours.clear();
        DonationType type = getSpinnerValue();
        String donationType = "";
        if(type.getName().equals("Don de sang")) {
            donationType = "blood";
        } else if(type.getName().equals("Don de plasma")) {
            donationType = "plasma";
        } else if(type.getName().equals("Don de plaquettes")) {
            donationType = "plaquettes";
        }
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("donation_center");
        appointmentRef.child(((AppointmentActivity)getActivity()).getCenterId()).child("appointments").child(donationType).child(selectedDate.replace("/", "-"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot hourSnapshot : snapshot.getChildren()) {
                    String hour = hourSnapshot.getKey();
                    for(Hour h : allHours) {
                        if(h.toString().equals(hour)) {
                            takenHours.add(h);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public DonationType getSpinnerValue() {
        if(binding.donationsSpinner.getSelectedItem().toString().equals("Don de sang")) {
            return DonationType.BLOOD;
        } else if(binding.donationsSpinner.getSelectedItem().toString().equals("Don de plasma")) {
            return DonationType.PLASMA;
        } else if(binding.donationsSpinner.getSelectedItem().toString().equals("Don de plaquettes")) {
            return DonationType.PLAQUETTES;
        }
        return null;
    }
}
