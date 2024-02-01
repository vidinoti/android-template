package com.vidinoti.vdarsdk;

public interface MapBoxListener {
    void mapLoaded();

    void markerClicked(String id);

    void pointClicked(String id, double latitude, double longitude, double detectionRadius);

    void mapError();
}
