package com.example.kamranali.geofencefinale;

/**
 * Created by kamranali on 13/01/2017.
 */

public class Model {
    private double longitued;
    private double latitude;
    private float radius;
    private String pushkey;
    private String placeName;

    public Model(double longitued, double latitude, float radius, String pushkey, String placeName) {
        this.longitued = longitued;
        this.latitude = latitude;
        this.radius = radius;
        this.pushkey = pushkey;
        this.placeName = placeName;
    }


    public Model() {
    }

    @Override
    public String toString() {
        return "Model{ " +
                "longitued= " + longitued +
                ", latitude= " + latitude +
                ", radius= " + radius +
                ", pushkey=' " + pushkey + '\'' +
                '}';
    }

    public double getLongitued() {
        return longitued;
    }

    public void setLongitued(double longitued) {
        this.longitued = longitued;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
