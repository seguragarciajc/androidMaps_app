package com.example.comov;

import android.app.Activity;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class LocationHelper {
    public static Location location = null;

    public static FusedLocationProviderClient getLocationFusedInstance(Activity activity){
        return LocationServices.getFusedLocationProviderClient(activity);
    }

    public static LocationRequest createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return  locationRequest;
    }
}
