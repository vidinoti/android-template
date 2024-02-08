package com.vidinoti.vdarsdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.LinkedList;

public abstract class MapBoxFragment extends WebFragment implements MapBoxListener {

    private boolean mapLoaded = false;
    private final LinkedList<String> afterMapLoadScripts = new LinkedList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public MapBoxFragment() {
        this.url = "https://appassets.androidplatform.net/assets/vidinoti-mapbox/index.html";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWebView().getSettings().setJavaScriptEnabled(true);

        getWebView().addJavascriptInterface(new MapBoxWebInterface(view.getContext(),
                        getConfiguration(), this),
                "VidinotiMapBoxInterface");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    setUserLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };

        askGeolocationPermissionIfNeeded();
    }

    private void askGeolocationPermissionIfNeeded() {
        if (!hasLocationPermission()) {
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                                    .RequestMultiplePermissions(), result -> {
                                Boolean fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                                Boolean coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                                if (fineLocationGranted != null && fineLocationGranted) {
                                    startGeotracking();
                                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                    startGeotracking();
                                }
                            }
                    );
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasLocationPermission()) {
            startGeotracking();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopGeotracking();
    }

    private boolean hasLocationPermission() {
        Context context = getContext();
        if (context != null) {
            return ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void getLastPosition() {
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setDurationMillis(3000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .build();
        fusedLocationClient.getCurrentLocation(request, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        setUserLocation(location.getLatitude(), location.getLongitude());
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void startGeotracking() {
        getLastPosition();
        LocationRequest locationRequest = new LocationRequest.Builder(3000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(1000)
                .build();
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void stopGeotracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void beforeLoadUrl() {
        startLoading();
    }

    public void setUserLocation(double latitude, double longitude) {
        evaluateJavascript("window.VidinotiMap.setUserPosition(" + latitude + "," + longitude + ")");
    }

    public void showTour(VidinotiTour tour) {
        evaluateJavascript("window.VidinotiMap.setTour(" + tour.toJSON() + ")");
    }

    public void flyTo(double latitude, double longitude) {
        evaluateJavascript("window.VidinotiMap.flyTo(" + latitude + "," + longitude + ")");
    }

    public void zoomFitMarkers() {
        evaluateJavascript("window.VidinotiMap.zoomFitMarkers()");
    }

    @Override
    public void mapError() {
        runOnUiThread(this::stopLoading);
    }

    @Override
    public void mapLoaded() {
        runOnUiThread(this::stopLoading);
        mapLoaded = true;
        String js = afterMapLoadScripts.poll();
        while (js != null) {
            evaluateJavascript(js);
            js = afterMapLoadScripts.poll();
        }
    }

    @Override
    public void markerClicked(String id) {
        // To be overridden
    }

    @Override
    public void pointClicked(String id, double latitude, double longitude, double detectionRadius) {
        // To be overridden
    }

    private void evaluateJavascript(String js) {
        if (mapLoaded) {
            runOnUiThread(() -> getWebView().evaluateJavascript(js, null));
        } else {
            afterMapLoadScripts.add(js);
        }
    }

    @Override
    protected WebViewClient getWebViewClient() {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        return new WebViewClientCompat() {
            private final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                    .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                    .build();

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                if (request.getUrl().toString().startsWith("https://appassets.androidplatform.net/")) {
                    return false;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(browserIntent);
                return true;
            }
        };
    }

    public abstract MapBoxConfig getConfiguration();
}
