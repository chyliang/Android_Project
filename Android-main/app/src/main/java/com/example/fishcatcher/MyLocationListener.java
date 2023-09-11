package com.example.fishcatcher;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationListener implements LocationListener {

    Context context;

    double latitude;
    double longitude;
    String city;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void getSetCityNameFromCoordinates() {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality()); //debug
                setCity(addresses.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onLocationChanged(Location loc) {
        setLatitude(loc.getLatitude());
        setLongitude(loc.getLongitude());
        getSetCityNameFromCoordinates();
    }
}
