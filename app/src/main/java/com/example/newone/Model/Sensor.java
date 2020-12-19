package com.example.newone.Model;

public class Sensor {
    private int id;
    private int sensorStatus;
    private int roadID;



    public Sensor(int id, int sensorStatus, int roadID) {
        this.id = id;
        this.sensorStatus = sensorStatus;
        this.roadID = roadID;
    }

    public Sensor(int sensorStatus, int roadID) {
        this.sensorStatus = sensorStatus;
        this.roadID = roadID;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(int sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public int getRoadID() {
        return roadID;
    }

    public void setRoadID(int roadID) {
        this.roadID = roadID;
    }
}
