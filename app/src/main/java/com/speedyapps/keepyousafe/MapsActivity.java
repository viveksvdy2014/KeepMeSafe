package com.speedyapps.keepyousafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sp;
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        sp=getSharedPreferences("locationinfo",MODE_PRIVATE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                if(Double.parseDouble(sp.getString("latitude","0.0"))!=latitude&&!(sp.getString("latitude","null").equals("null"))) {
                    Toast.makeText(MapsActivity.this, "Calling updateMap", Toast.LENGTH_SHORT).show();
                    updateLocation();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(run);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sp.edit().putString("status","running").commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sp.edit().putString("status","stopped").commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sp.edit().putString("status","paused").commit();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;
        updateLocation();
    }

    public void updateLocation(){
        latitude=Double.parseDouble(sp.getString("latitude","0.00"));
        longitude=Double.parseDouble(sp.getString("longitude","0.00"));
        Toast.makeText(this, ""+latitude+longitude, Toast.LENGTH_SHORT).show();
        if(latitude!=0.00) {
            LatLng currentLocation = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Last known Location !"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18));
         }
    }
}
