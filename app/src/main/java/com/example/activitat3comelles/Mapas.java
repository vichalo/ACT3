package com.example.activitat3comelles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Mapas extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    Button button;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    LatLng pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        button = findViewById(R.id.button2);
        button.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera
        mMap.getMyLocation();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera

    }

    private void initCoordenadas() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        pos = new LatLng(location.getLatitude(), location.getLongitude());
                        try {
                            mMap.addMarker(new MarkerOptions().position(pos).title("Població: "+getPoblacio(pos)+" Adreça: "+getAdresa(pos)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                    }
                });
    }

    public String getPoblacio(LatLng a) throws IOException {

            Geocoder geocoder = new Geocoder(Mapas.this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(a.latitude,a.longitude,1);

            if (addressList.size()>0){
                String poblacio = String.valueOf(addressList.get(0).getLocality());
                return poblacio;
            }

        return "try again";

    }
    public String getAdresa(LatLng a) throws IOException {

        Geocoder geocoder = new Geocoder(Mapas.this, Locale.getDefault());
        List<Address> addressList = geocoder.getFromLocation(a.latitude,a.longitude,1);
        if (addressList.size()>0){
            String poblacio = String.valueOf(addressList.get(0).getAddressLine(0));
            return poblacio;
        }
        return "try again";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                initCoordenadas();
                break;

        }
    }
}