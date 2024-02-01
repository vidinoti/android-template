package com.vidinoti.vdarsdk;

public class MapBoxConfig {

    private final String accessToken;
    private double centerLatitude = 47;
    private double centerLongitude = 7.5;
    private int zoom = 9;

    public MapBoxConfig(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public int getZoom() {
        return zoom;
    }
}
