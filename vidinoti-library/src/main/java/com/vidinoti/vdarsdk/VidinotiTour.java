package com.vidinoti.vdarsdk;

import com.google.gson.Gson;

public class VidinotiTour {
    private long id;
    private String name;
    private String pathUrl;
    private Double latitude;
    private Double longitude;
    private VidinotiTourPoint points[];

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public VidinotiTourPoint[] getPoints() {
        return points;
    }

    public void setPoints(VidinotiTourPoint[] points) {
        this.points = points;
    }
}
