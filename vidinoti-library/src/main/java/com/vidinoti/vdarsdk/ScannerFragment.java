package com.vidinoti.vdarsdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.vidinoti.vdarsdk.view.VidinotiProgressView;


public class ScannerFragment extends BaseScannerFragment {

    private View viewfinderView;

    /**
     * View placed above the camera view. It can be used for adding a watermark.
     */
    private View overlayView;
    
    private VidinotiProgressView progressView;

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vidinoti_scanner_fragment, container, false);
        progressView = view.findViewById(R.id.progressView);
        viewfinderView = view.findViewById(R.id.viewfinderView);
        overlayView = view.findViewById(R.id.overlayView);
        return view;
    }

    @Override
    public int getAnnotationViewId() {
        return R.id.annotationView;
    }

    @Override
    public void onPause() {
        super.onPause();
        viewfinderView.setVisibility(View.VISIBLE);
        overlayView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPresentAnnotations() {
        super.onPresentAnnotations();
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                viewfinderView.setVisibility(View.GONE);
                overlayView.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void onAnnotationsHidden() {
        super.onAnnotationsHidden();
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                viewfinderView.setVisibility(View.VISIBLE);
                overlayView.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onSyncProgress(int progress) {
        super.onSyncProgress(progress);
        progressView.setProgress(progress);
        if (progress < 100) {
            viewfinderView.setVisibility(View.GONE);
            overlayView.setVisibility(View.GONE);
        } else {
            viewfinderView.setVisibility(View.VISIBLE);
            overlayView.setVisibility(View.VISIBLE);
        }
    }
}
