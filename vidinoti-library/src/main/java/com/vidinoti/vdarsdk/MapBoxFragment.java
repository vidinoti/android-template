package com.vidinoti.vdarsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

public abstract class MapBoxFragment extends WebFragment implements MapBoxListener {

    private boolean mapLoaded = false;
    private final LinkedList<String> afterMapLoadScripts = new LinkedList<>();

    public MapBoxFragment() {
        this.url = "https://appassets.androidplatform.net/assets/vidinoti-mapbox/index.html";
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWebView().getSettings().setJavaScriptEnabled(true);

        getWebView().addJavascriptInterface(new MapBoxWebInterface(view.getContext(),
                        getConfiguration(), this),
                "VidinotiMapBoxInterface");
    }

    @Override
    public void beforeLoadUrl() {
        startLoading();
    }

    // TODO geolocation

    public void setUserLocation(double latitude, double longitude) {
        evaluateJavascript("window.VidinotiMap.setUserPosition(" + latitude + "," + longitude + ")");
    }

    public void showTour(VidinotiTour tour) {
        evaluateJavascript("window.VidinotiMap.setTour(" + tour.toJSON() + ")");
    }

    public void flyTo(double latitude, double longitude) {
        evaluateJavascript("window.VidinotiMap.flyTo(" + latitude + "," + longitude +")");
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
