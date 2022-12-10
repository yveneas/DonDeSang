package com.example.dondesang.ui.location;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dondesang.AppointmentActivity;
import com.example.dondesang.R;
import com.example.dondesang.dao.DonationCenterDAO;
import com.example.dondesang.databinding.FragmentLocationBinding;
import com.example.dondesang.model.DonationCenter;
import com.example.dondesang.model.DonationType;
import com.example.dondesang.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentLocationBinding binding;
    private List<DonationCenter> donationCenters;
    private DonationCenterDAO donationCenterDAO;
    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout bottomSheet;
    private FirebaseAuth mAuth;
    private User user;
    private String type;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLocationBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        bottomSheet = binding.getRoot().findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheet.findViewById(R.id.closeSheetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        donationCenterDAO = new DonationCenterDAO();
        donationCenters = new ArrayList<>();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listener();
        if(mAuth != null) {
            DatabaseReference donationCenterRef = FirebaseDatabase.getInstance().getReference("donation_center");
            donationCenterRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        for(DataSnapshot donationCenter : snapshot.getChildren()) {
                            donationCenters.add(donationCenter.getValue(DonationCenter.class));
                        }
                        for(DonationCenter donationCenter : donationCenters) {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> list = geocoder.getFromLocationName(donationCenter.getAddress(), 1);
                                if(list.size() > 0) {
                                    Address address = list.get(0);
                                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                                    marker.setTitle(donationCenter.getName());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            userRef.child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                    }
                }
            });
        }
        /*
        List<DonationType> donationTypes = new ArrayList<>();
        donationTypes.add(DonationType.BLOOD);
        donationTypes.add(DonationType.PLASMA);
        donationTypes.add(DonationType.PLAQUETTES);

        DonationCenter donationCenter2 = new DonationCenter("Plasmavie Gatineau", "205 rue Bellehumeur  Gatineau, Qc J8T 8H3", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Kirkland", "16993 route Transcanadienne  Kirkland, Qc H9H 5J1", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Dix30", "9370 boul. Leduc  Brossard, Qc J4Y 0B3", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Laval", "1600 boul. Le Corbusier  Laval, Qc H7S 1Y9", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Versailles", "7275 rue Sherbrooke Est  Montréal, Qc H1N 1E9", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("Centre de donneurs Héma-Québec de Saint-Bruno", "1 boulevard des Promenades  Saint-Bruno-de-Montarville, Qc J3V 5J5", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Lebourgneuf", "1020 rue Bouvier  Québec, Qc G2K 0K9", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("GLOBULE Sainte-Foy", "2828 boulevard Laurier  Québec, Qc G1V 0C1", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("PLASMAVIE Saguenay", "2096 boulevard Talbot  Chicoutimi, Qc G7H 8B5", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);
        donationCenter2 = new DonationCenter("PLASMAVIE Trois-Rivières", "4767 boulevard Gene-H. Kruger  Trois-Rivières, Qc G9A 4N4", donationTypes);
        donationCenterDAO.addDonationCenter(donationCenter2);*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listener() {
        binding.searchLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = binding.searchLocationField.getText().toString();
                if(!location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> list = geocoder.getFromLocationName(location, 1);
                        if(list.size() > 0) {
                            Address address = list.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {

                for(DonationCenter donationCenter : donationCenters) {
                    if(donationCenter.getName().equals(marker.getTitle())) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        TextView text = bottomSheet.findViewById(R.id.placeText);
                        text.setText(donationCenter.getName());


                        for(DonationType donationType : donationCenter.getDonationTypes()) {
                            LinearLayout bloodLayout = bottomSheet.findViewById(R.id.bloodDonationLayout);
                            LinearLayout plasmaLayout = bottomSheet.findViewById(R.id.plasmaDonationLayout);
                            LinearLayout plaquetteLayout = bottomSheet.findViewById(R.id.plaquettesDonationLayout);
                            if(donationType == DonationType.BLOOD) {

                                bloodLayout.setVisibility(View.VISIBLE);

                                ImageButton bloodButton = bloodLayout.findViewById(R.id.bloodDonationButton);
                                bloodButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bloodLayout.setBackground(getResources().getDrawable(R.drawable.calendar_background));
                                        plasmaLayout.setBackground(null);
                                        plaquetteLayout.setBackground(null);
                                        type = "sang";
                                        Button appointmentButton = bottomSheet.findViewById(R.id.appointmentButton);
                                        if(user.isDonationPossible(DonationType.BLOOD)) {
                                            appointmentButton.setEnabled(true);
                                        } else {
                                            appointmentButton.setEnabled(false);
                                        }

                                    }
                                });

                            }
                            if(donationType == DonationType.PLASMA) {

                                plasmaLayout.setVisibility(View.VISIBLE);

                                ImageButton plasmaButton = plasmaLayout.findViewById(R.id.plasmaDonationButton);
                                plasmaButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        plasmaLayout.setBackground(getResources().getDrawable(R.drawable.calendar_background));
                                        bloodLayout.setBackground(null);
                                        plaquetteLayout.setBackground(null);
                                        type = "plasma";
                                        Button appointmentButton = bottomSheet.findViewById(R.id.appointmentButton);
                                        if(user.isDonationPossible(DonationType.PLASMA)) {
                                            appointmentButton.setEnabled(true);
                                        } else {
                                            appointmentButton.setEnabled(false);
                                        }

                                    }
                                });
                            }
                            if(donationType == DonationType.PLAQUETTES) {

                                plaquetteLayout.setVisibility(View.VISIBLE);

                                ImageButton plaquetteButton = plaquetteLayout.findViewById(R.id.plaquetteDonationButton);
                                plaquetteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        plaquetteLayout.setBackground(getResources().getDrawable(R.drawable.calendar_background));
                                        plasmaLayout.setBackground(null);
                                        bloodLayout.setBackground(null);
                                        type = "plaquettes";
                                        Button appointmentButton = bottomSheet.findViewById(R.id.appointmentButton);
                                        if(user.isDonationPossible(DonationType.PLAQUETTES)) {
                                            appointmentButton.setEnabled(true);
                                        } else {
                                            appointmentButton.setEnabled(false);
                                        }

                                    }
                                });
                            }

                        }

                        Button appointmentButton = bottomSheet.findViewById(R.id.appointmentButton);
                        appointmentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference centerRef = FirebaseDatabase.getInstance()
                                        .getReference("donation_center");
                                centerRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(DataSnapshot snapshot : task.getResult().getChildren()) {
                                                DonationCenter donationCenter1 = snapshot.getValue(DonationCenter.class);
                                                if(donationCenter1.getName().equals(donationCenter.getName())) {
                                                    Intent intent = new Intent(getContext(), AppointmentActivity.class);
                                                    System.out.println("TEST " + snapshot.getKey());
                                                    intent.putExtra("centerId", snapshot.getKey());
                                                    intent.putExtra("type", type);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        //Faire en sorte de ne pas pouvoir prendre de rdv quand le delai n'est pas dépassé
                        //Toast.makeText(getContext(), donationCenter.getName() + " : " + donationCenter.getAddress(), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}