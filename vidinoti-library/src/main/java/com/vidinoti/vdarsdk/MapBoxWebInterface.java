package com.vidinoti.vdarsdk;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import java.util.Map;

public class MapBoxWebInterface {

    private final Context context;
    private final MapBoxConfig config;
    private final MapBoxListener listener;

    public MapBoxWebInterface(Context context, MapBoxConfig config, MapBoxListener listener) {
        this.context = context.getApplicationContext();
        this.config = config;
        this.listener = listener;
    }

    @JavascriptInterface
    public String getAppID() {
        return this.context.getPackageName();
    }

    @JavascriptInterface
    public String getLicenseKey() {
        return VidinotiAR.getInstance().getOptions().getLicenseKey();
    }

    @JavascriptInterface
    public String getMapBoxAccessToken() {
        return this.config.getAccessToken();
    }

    @JavascriptInterface
    public double getCenterLatitude() {
        return config.getCenterLatitude();
    }

    @JavascriptInterface
    public double getCenterLongitude() {
        return config.getCenterLongitude();
    }

    @JavascriptInterface
    public int getZoom() {
        return config.getZoom();
    }

    @JavascriptInterface
    public void mapLoaded() {
        this.listener.mapLoaded();
    }

    @JavascriptInterface
    public void markerClicked(String id) {
        this.listener.markerClicked(id);
    }

    @JavascriptInterface
    public void pointClicked(String id, double latitude, double longitude, double detectionRadius) {
        this.listener.pointClicked(id, latitude, longitude, detectionRadius);
    }

    @JavascriptInterface
    public void mapError() {
        this.listener.mapError();
    }

    @JavascriptInterface
    public String getTexts() {
        Map<String, String> texts = config.getTexts();
        if (texts == null) {
            return "{}";
        }
        return new Gson().toJson(texts);
    }
}
