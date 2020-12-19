package com.example.newone.Model;


// for favorite places in the map
public class Place {

    private int id;
    private String platitude;
    private String pLongitude;
    private String title;

    public Place() {

    }

    public Place(int id, String platitude, String pLongitude, String title) {
        this.id = id;
        this.platitude = platitude;
        this.pLongitude = pLongitude;
        this.title = title;
    }

    public Place(String platitude, String pLongitude, String title) {
        this.platitude = platitude;
        this.pLongitude = pLongitude;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatitude() {
        return platitude;
    }

    public void setPlatitude(String platitude) {
        this.platitude = platitude;
    }

    public String getpLongitude() {
        return pLongitude;
    }

    public void setpLongitude(String pLongitude) {
        this.pLongitude = pLongitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
