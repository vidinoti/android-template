package com.vidinoti.vdarsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vidinoti.android.vdarsdk.VDARContextPrior;
import com.vidinoti.android.vdarsdk.VDARIntersectionPrior;
import com.vidinoti.android.vdarsdk.VDARPrior;
import com.vidinoti.android.vdarsdk.VDARRemoteController;
import com.vidinoti.android.vdarsdk.VDARRemoteControllerListener;
import com.vidinoti.android.vdarsdk.VDARSDKController;
import com.vidinoti.android.vdarsdk.VDARTagPrior;
import com.vidinoti.android.vdarsdk.geopoint.GeoPointManager;
import com.vidinoti.android.vdarsdk.geopoint.VDARGPSPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class VidinotiAR implements VDARRemoteControllerListener {

    public interface VidinotiEventListener {
        void onTagQrCodeScanned(String tagName);
        void onContentQrCodeScanned(String id);
    }

    private static final String TAG = VidinotiAR.class.getName();

    private static final String STORAGE_KEY_ADDITIONAL_CONTENT_ID = "com.vidinoti.vdarsdk.ADDITIONAL_CONTENT";
    private static final String STORAGE_KEY_ADDITIONAL_CONTENT_TAG = "com.vidinoti.vdarsdk.ADDITIONAL_TAG";

    /**
     * Interval between the synchronization when the application is opened (30 minutes)
     */
    private static final long MIN_SYNC_INTERVAL = 1000 * 60 * 15;

    private static VidinotiAR instance = null;

    public static VidinotiAR init(Context context, VidinotiAROptions options) {
        if (instance != null) {
            Log.w(TAG, "VidinotiAR has already been initialized");
            return instance;
        }
        File vidinotiSdkFolder = new File(context.getFilesDir(), "vidinotiSdk");
        String storageDir = vidinotiSdkFolder.getAbsolutePath();
        VDARSDKController controller = VDARSDKController.startSDK(context.getApplicationContext(), storageDir, options.getLicenseKey());
        controller.setEnableCodesRecognition(options.isCodeRecognitionEnabled());
        instance = new VidinotiAR(context.getApplicationContext(), controller, options);
        return instance;
    }

    @NonNull
    public static VidinotiAR getInstance() {
        if (instance == null) {
            throw new NullPointerException("VidinotiAR has not been correctly initialized");
        }
        return instance;
    }

    private final VidinotiAROptions options;
    private final VDARSDKController controller;
    private final AtomicBoolean syncInProgress = new AtomicBoolean();
    private final VidinotiStorage storage;
    private final List<VidinotiEventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final List<VidinotiSynchronizationProgressListener> syncProgressListeners = new CopyOnWriteArrayList<>();
    private long lastSyncTimestamp = 0;
    private int syncProgress = 0;

    private VidinotiAR(Context context, VDARSDKController controller, VidinotiAROptions options) {
        this.options = options;
        this.controller = controller;
        this.storage = new VidinotiStorage(context);
        VDARRemoteController.getInstance().addProgressListener(this);
    }

    public void synchronize() {
        if (syncInProgress.compareAndSet(false, true)) {
            lastSyncTimestamp = System.currentTimeMillis();
            final List<VDARPrior> priors = getSyncPriors();
            controller.addNewAfterLoadingTask(() -> VDARRemoteController.getInstance().syncRemoteContextsAsynchronouslyWithPriors(priors, (observable, data) -> {
                VDARRemoteController.ObserverUpdateInfo info = (VDARRemoteController.ObserverUpdateInfo) data;
                if (info.isCompleted()) {
                    syncInProgress.set(false);
                    String error = info.getError();
                    if (error != null && !error.isEmpty()) {
                        Log.w(TAG, "Synchronization error: " + error);
                        for (VidinotiSynchronizationProgressListener listener: syncProgressListeners) {
                            listener.onSyncError(error);
                        }
                    }
                    Log.v(TAG, "Synchronization over. Synced " + info.getFetchedContexts().size() + " contents");
                }
            }));
        }
    }

    public void synchronizeIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastSyncTimestamp > MIN_SYNC_INTERVAL) {
            VidinotiAR.getInstance().synchronize();
        }
    }

    private List<VDARPrior> getSyncPriors() {
        List<VDARPrior> priors = new LinkedList<>();
        String additionalContent = getAdditionalContent();
        if (additionalContent != null) {
            priors.add(new VDARContextPrior(additionalContent));
        }
        switch (options.getSynchronizationMode()) {
            case DEFAULT_TAG:
                priors.add(getTagPrior(options.getDefaultTag()));
                break;
            case DEFAULT_TAG_WITH_ADDITIONAL_CONTENT:
                priors.add(getTagPrior(options.getDefaultTag()));
                String additionalTag = getAdditionalTag();
                if (additionalTag != null) {
                    priors.add(getTagPrior(additionalTag));
                }
                break;
            case LANGUAGE_ONLY:
                priors.add(getLanguageTag());
                break;
            default:
                if (options.isMultilingualEnabled()) {
                    priors.add(getLanguageTag());
                } else {
                    priors = null;
                }
        }
        return priors;
    }

    private VDARPrior getTagPrior(String tagName) {
        if (options.isMultilingualEnabled()) {
            ArrayList<VDARPrior> intersection = new ArrayList<>();
            intersection.add(new VDARTagPrior(tagName));
            intersection.add(getLanguageTag());
            return new VDARIntersectionPrior(intersection);
        } else {
            return new VDARTagPrior(tagName);
        }
    }

    private VDARTagPrior getLanguageTag() {
        String code = Locale.getDefault().getLanguage();
        VidinotiLanguage deviceLanguage;
        try {
            deviceLanguage = VidinotiLanguage.valueOf(code.toUpperCase());
        } catch (Exception e) {
            deviceLanguage = null;
        }
        if (deviceLanguage != null && options.getSupportedLanguages().contains(deviceLanguage)) {
            return new VDARTagPrior(deviceLanguage.getTagName());
        }
        return new VDARTagPrior(options.getDefaultLanguage().getTagName());
    }

    public String getAdditionalTag() {
        return storage.getString(STORAGE_KEY_ADDITIONAL_CONTENT_TAG);
    }

    public void removeAdditionalTag() {
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT_TAG, null);
    }

    public String getAdditionalContent() {
        return storage.getString(STORAGE_KEY_ADDITIONAL_CONTENT_ID);
    }

    public void removeAdditionalContent() {
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT_ID, null);
    }

    protected void onTagQrCodeScanned(String tagName) {
        if (tagName == null) {
            return;
        }
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT_TAG, tagName);
        synchronize();
        for (VidinotiEventListener listener : eventListeners) {
            listener.onTagQrCodeScanned(tagName);
        }
    }

    protected void onContentQrCodeScanned(String contextId) {
        if (contextId == null) {
            return;
        }
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT_ID, contextId);
        synchronize();
        for (VidinotiEventListener listener : eventListeners) {
            listener.onContentQrCodeScanned(contextId);
        }
    }

    public void registerListener(VidinotiEventListener listener) {
        eventListeners.add(listener);
    }

    public void unregisterListener(VidinotiEventListener listener) {
        eventListeners.remove(listener);
    }

    @Override
    public void onSyncProgress(VDARRemoteController remoteController, float percent, boolean ready, String folder) {
        int progress = (int) percent;
        syncProgress = progress;
        for (VidinotiSynchronizationProgressListener listener : syncProgressListeners) {
            listener.onSyncProgress(progress);
        }
    }

    /**
     * Registers a synchronization progress listener. When the listener is added, it is directly called with the current
     * synchronization progress (e.g. the listener is called with 100 if the synchronization is not running)
     *
     * @param listener the listener
     */
    public void addProgressListener(VidinotiSynchronizationProgressListener listener) {
        syncProgressListeners.add(listener);
        if (syncInProgress.get()) {
            listener.onSyncProgress(syncProgress);
        } else {
            listener.onSyncProgress(100);
        }
    }

    /**
     * Unregisters the synchronization listener.
     *
     * @param listener the listener
     */
    public void removeProgressListener(VidinotiSynchronizationProgressListener listener) {
        syncProgressListeners.remove(listener);
    }

    public List<VDARGPSPoint> getAllGpsPoints() {
        return GeoPointManager.getGPSPointsInBoundingBox(-90, -180, 90, 180);
    }

    /**
     * Returns true if a synchronization is running, false otherwise.
     *
     * @return true if a synchronization is running, false otherwise.
     */
    public boolean isSyncInProgress() {
        return syncInProgress.get();
    }

    public VidinotiAROptions getOptions() {
        return options;
    }
}
