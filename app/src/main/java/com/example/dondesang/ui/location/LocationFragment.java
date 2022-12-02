package com.example.dondesang.ui.location;



import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dondesang.dao.DonationCenterDAO;
import com.example.dondesang.model.DonationCenter;
import com.example.dondesang.model.DonationType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.databinding.FragmentLocationBinding;
import com.example.dondesang.ui.account.menu.MenuFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentLocationBinding binding;
    private List<DonationCenter> donationCenters;
    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout bottomSheet;
    private DonationCenterDAO donationCenterDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLocationBinding.inflate(inflater, container, false);

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
        donationCenters = donationCenterDAO.getAllDonationCenter();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listener();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
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

                        LinearLayout linearLayout = bottomSheet.findViewById(R.id.availableDonationsLayout);
                        linearLayout.removeAllViews();
                        for(DonationType donationType : donationCenter.getDonationTypes()) {
                            if(donationType == DonationType.BLOOD) {
                                LinearLayout linearLayout1 = new LinearLayout(getContext());
                                linearLayout1.setOrientation(LinearLayout.VERTICAL);

                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageResource(R.drawable.blood_icon);

                                TextView textView = new TextView(getContext());
                                textView.setText("Don de sang");
                                textView.setTextColor(getResources().getColor(R.color.main_color));

                                linearLayout1.addView(imageView);
                                linearLayout1.addView(textView);
                                linearLayout1.setPadding(0, 0, 20, 0);
                                linearLayout.addView(linearLayout1);
                            }
                            if(donationType == DonationType.PLASMA) {
                                LinearLayout linearLayout1 = new LinearLayout(getContext());
                                linearLayout1.setOrientation(LinearLayout.VERTICAL);

                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageResource(R.drawable.plasma_icon);

                                TextView textView = new TextView(getContext());
                                textView.setText("Don de plasma");
                                textView.setTextColor(getResources().getColor(R.color.main_color));

                                linearLayout1.addView(imageView);
                                linearLayout1.addView(textView);
                                linearLayout1.setPadding(0, 0, 20, 0);
                                linearLayout.addView(linearLayout1);
                            }
                            if(donationType == DonationType.PLAQUETTES) {
                                LinearLayout linearLayout1 = new LinearLayout(getContext());
                                linearLayout1.setOrientation(LinearLayout.VERTICAL);

                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageResource(R.drawable.plaquette_icon);

                                TextView textView = new TextView(getContext());
                                textView.setText("Don de plaquettes");
                                textView.setTextColor(getResources().getColor(R.color.main_color));

                                linearLayout1.addView(imageView);
                                linearLayout1.addView(textView);
                                linearLayout1.setPadding(0, 0, 20, 0);
                                linearLayout.addView(linearLayout1);
                            }

                        }
                        //Toast.makeText(getContext(), donationCenter.getName() + " : " + donationCenter.getAddress(), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}