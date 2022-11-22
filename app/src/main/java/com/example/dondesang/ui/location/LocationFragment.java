package com.example.dondesang.ui.location;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.dondesang.R;
import com.example.dondesang.UserActivity;
import com.example.dondesang.databinding.FragmentLocationBinding;
import com.example.dondesang.ui.account.menu.MenuFragment;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentLocationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocationViewModel locationViewModel =
                new ViewModelProvider(this).get(LocationViewModel.class);

        binding = FragmentLocationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLocation;
        locationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        listener();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listener() {
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Marqueurs exemple pour version alpha
        LatLng paris= new LatLng(48, 2.3);
        LatLng marseille= new LatLng(43, 5);
        LatLng brest= new LatLng(48, -4);
        LatLng bordeaux= new LatLng(44, 0);
        LatLng strasbourg= new LatLng(48, 7);
        mMap.addMarker(new MarkerOptions().position(paris).title("Centre de don Paris"));
        mMap.addMarker(new MarkerOptions().position(marseille).title("Centre de don Marseille"));
        mMap.addMarker(new MarkerOptions().position(brest).title("Centre de don Brest"));
        mMap.addMarker(new MarkerOptions().position(bordeaux).title("Centre de don Bordeaux"));
        mMap.addMarker(new MarkerOptions().position(strasbourg).title("Centre de don Strasbourg"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48,2.3) , 5.0f));
    }
}