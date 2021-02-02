package com.vidinoti.vdarsdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import java.io.IOException;
import java.util.ArrayList;


public abstract class BaseScannerFragment extends Fragment implements VDARSDKControllerEventReceiver,
        VidinotiSynchronizationProgressListener {

    private static final String TAG = BaseScannerFragment.class.getName();

    private static final String QR_CODE_TAG_PREFIX = "pixliveplayer/";
    private static final String QR_CODE_CONTEXT_PREFIX = "context/";

    private ScannerFragmentListener listener;
    private VDARAnnotationView annotationView;

    public BaseScannerFragment() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        annotationView = view.findViewById(getAnnotationViewId());
        annotationView.setDarkScreenMode(false);
        annotationView.setAnimationSpeed(0);
    }

    public abstract int getAnnotationViewId();

    @SuppressWarnings("unused")
    public VDARAnnotationView getAnnotationView() {
        return annotationView;
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
            } else if (VidinotiAR.getInstance().getOptions().isCodeRecognitionOpenURL() &&
                    (code.startsWith("http://") || code.startsWith("https://"))) {
                VidinotiUtils.openUrlInBrowser(getContext(), code);
            } else if (code.startsWith(QR_CODE_CONTEXT_PREFIX)) {
                VidinotiAR.getInstance().onContentQrCodeScanned(code.substring(QR_CODE_CONTEXT_PREFIX.length()));
            }
        }
    }

    @Override
    public void onFatalError(String s) {
        Log.e(TAG, "Vidinoti SDK fatal error: " + s);
    }

    @Override
    public void onPresentAnnotations() {
        if (listener != null) {
            listener.onPresentAnnotations();
        }
    }

    @Override
    public void onAnnotationsHidden() {
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

    }
}
