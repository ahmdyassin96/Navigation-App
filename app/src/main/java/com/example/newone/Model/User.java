package com.example.newone.Model;

import com.google.android.gms.maps.model.LatLng;


// users in the app
public class User {

    private static int id=1;
    private String username;
    private String password;
    private LatLng location;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        //this.location = location;
        this.id=id;
        id++;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LatLng getLocation() {
        return location;
    }



    public void setLocation(LatLng location) {

    }
}
