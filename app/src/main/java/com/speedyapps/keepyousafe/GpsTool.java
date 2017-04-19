package com.speedyapps.keepyousafe;

/**
 * Created by SUBASH on 18-04-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by pethoalpar on 4/13/2016.
 */
public class GpsTool {

    String locationProvider = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public GpsTool(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.turnOnGps(context);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                onGpsLocationChanged(location);
                location.reset();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                onGpsStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                onGpsProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                onGpsProviderDisabled(provider);
            }
        };
        startGpsUpdate();
    }

    public void onGpsLocationChanged(Location location) {
        //Location change.
    }

    public void onGpsStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onGpsProviderEnabled(String provider) {

    }

    public void onGpsProviderDisabled(String provider) {

    }

    public Location getLocation(){
        return locationManager.getLastKnownLocation(locationProvider);
    }

    public void startGpsUpdate(){
        locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
    }

    public void stopGpsUpdate(){
        locationManager.removeUpdates(locationListener);
    }

    public void turnOnGps(Context context){
        boolean isEnabled = locationManager.isProviderEnabled(locationProvider);
        if(!isEnabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
    }
}
