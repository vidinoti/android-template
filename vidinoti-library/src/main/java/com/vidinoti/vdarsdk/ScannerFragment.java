package com.vidinoti.vdarsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vidinoti.android.vdarsdk.VDARAnnotationView;
import com.vidinoti.android.vdarsdk.VDARCode;
import com.vidinoti.android.vdarsdk.VDARContext;
import com.vidinoti.android.vdarsdk.VDARPrior;
import com.vidinoti.android.vdarsdk.VDARSDKController;
import com.vidinoti.android.vdarsdk.VDARSDKControllerEventReceiver;
import com.vidinoti.android.vdarsdk.camera.DeviceCameraImageSender;
import com.vidinoti.vdarsdk.view.VidinotiProgressView;

import java.io.IOException;
import java.util.ArrayList;


public class ScannerFragment extends Fragment implements VDARSDKControllerEventReceiver,
        VidinotiSynchronizationProgressListener {

    private static final String TAG = ScannerFragment.class.getName();

    private static final String QR_CODE_TAG_PREFIX = "pixliveplayer/";

    private ScannerFragmentListener listener;
    private VDARAnnotationView annotationView;
    private View overlayView;
    private VidinotiProgressView progressView;

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            new DeviceCameraImageSender();
        } catch (IOException e) {
            Log.e(TAG, "Error initializing camera");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ScannerFragmentListener) {
            listener = (ScannerFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vidinoti_scanner_fragment, container, false);
        annotationView = view.findViewById(R.id.annotationView);
        annotationView.setDarkScreenMode(false);
        annotationView.setAnimationSpeed(0);
        progressView = view.findViewById(R.id.progressView);
        overlayView = view.findViewById(R.id.overlayView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        annotationView.onResume();
        VidinotiAR.getInstance().addProgressListener(this);
        VDARSDKController.getInstance().registerEventReceiver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        annotationView.onPause();
        overlayView.setVisibility(View.VISIBLE);
        VidinotiAR.getInstance().removeProgressListener(this);
        VDARSDKController.getInstance().unregisterEventReceiver(this);
    }

    @Override
    public void onCodesRecognized(ArrayList<VDARCode> arrayList) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        VidinotiUtils.vibrate(context);
        if (arrayList != null && !arrayList.isEmpty()) {
            String code = arrayList.get(0).getCodeData();
            if (code == null) {
                return;
            }
            if (code.startsWith(QR_CODE_TAG_PREFIX)) {
                VidinotiAR.getInstance().onTagQrCodeScanned(code.substring(QR_CODE_TAG_PREFIX.length()));
            } else if (code.startsWith("http://") || code.startsWith("https://")) {
                VidinotiUtils.openUrlInBrowser(getContext(), code);
            }
        }
    }

    @Override
    public void onFatalError(String s) {
        Log.e(TAG, "Vidinoti SDK fatal error: " + s);
    }

    @Override
    public void onPresentAnnotations() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    overlayView.setVisibility(View.GONE);
                }
            });
        }
        if (listener != null) {
            listener.onPresentAnnotations();
        }
    }

    @Override
    public void onAnnotationsHidden() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    overlayView.setVisibility(View.VISIBLE);
                }
            });
        }
        if (listener != null) {
            listener.onAnnotationsHidden();
        }
    }

    @Override
    public void onTrackingStarted(int i, int i1) {

    }

    @Override
    public void onEnterContext(VDARContext vdarContext) {

    }

    @Override
    public void onExitContext(VDARContext vdarContext) {

    }

    @Override
    public void onRequireSynchronization(ArrayList<VDARPrior> arrayList) {
        Log.d(TAG, "Vidinoti SDK onRequireSynchronization - not implemented");
    }

    @Override
    public void onSyncProgress(int progress) {
        progressView.setProgress((int) progress);
        if (progress < 100) {
            overlayView.setVisibility(View.GONE);
        } else {
            overlayView.setVisibility(View.VISIBLE);
        }
    }
}
