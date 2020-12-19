package com.example.newone;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


// data for the route
public class Route {
    public Distance distance;
    public Duration duration;
    public String startAddress;
    public String endAddress;
    public LatLng startLocation;
    public LatLng endLocation;


    public List<LatLng> points;
}