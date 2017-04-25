package com.speedyapps.keepyousafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sp,settings;
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapText);
        mapFragment.getMapAsync(this);
        sp=getSharedPreferences("locationinfo",MODE_PRIVATE);
        settings=getSharedPreferences("settings",MODE_PRIVATE);
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
            Toast.makeText(this, ""+settings.getString("maptype","null"), Toast.LENGTH_SHORT).show();
            switch (settings.getString("maptype","null")){
                case "NORMAL":
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "HYBRID":
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "SATELITE":
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "TERRAIN":
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                default:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Last known Location !"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18));

         }
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(MapsActivity.this,MainActivity.class);
        startActivity(main);
        finish();
    }


}
