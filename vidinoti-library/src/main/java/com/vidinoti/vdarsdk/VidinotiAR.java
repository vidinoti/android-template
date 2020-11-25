package com.vidinoti.vdarsdk;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vidinoti.android.vdarsdk.VDARIntersectionPrior;
import com.vidinoti.android.vdarsdk.VDARPrior;
import com.vidinoti.android.vdarsdk.VDARRemoteController;
import com.vidinoti.android.vdarsdk.VDARSDKController;
import com.vidinoti.android.vdarsdk.VDARTagPrior;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class VidinotiAR {

    public interface VidinotiEventListener {
        void onTagQrCodeScanned(String tagName);
    }

    private static final String TAG = VidinotiAR.class.getName();

    private static final String STORAGE_KEY_ADDITIONAL_CONTENT = "com.vidinoti.vdarsdk.ADDITIONAL_TAG";

    /**
     * Interval between the synchronization when the application is opened (30 minutes)
     */
    private static final long MIN_SYNC_INTERVAL = 1000 * 60 * 15;

    private static VidinotiAR instance = null;

    private long lastSyncTimestamp = 0;

    public static VidinotiAR init(Context context, VidinotiAROptions options) {
        if (instance != null) {
            Log.w(TAG, "VidinotiAR has already been initialized");
            return instance;
        }
        File vidinotiSdkFolder = new File(context.getFilesDir(), "vidinotiSdk");
        String storageDir = vidinotiSdkFolder.getAbsolutePath();
        VDARSDKController controller = VDARSDKController.startSDK(context.getApplicationContext(), storageDir, options.getLicenseKey());
        controller.setEnableCodesRecognition(options.isCodeRecognitionEnabled());
        if (options.isNotificationSupportEnabled()) {
            controller.setNotificationsSupport(true);
        }
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

    private VidinotiAR(Context context, VDARSDKController controller, VidinotiAROptions options) {
        this.options = options;
        this.controller = controller;
        this.storage = new VidinotiStorage(context);
    }

    public void synchronize() {
        if (syncInProgress.compareAndSet(false, true)) {
            lastSyncTimestamp = System.currentTimeMillis();
            final List<VDARPrior> priors = getSyncPriors();
            controller.addNewAfterLoadingTask(new Runnable() {
                public void run() {
                    VDARRemoteController.getInstance().syncRemoteContextsAsynchronouslyWithPriors(priors, new Observer() {
                        public void update(Observable observable, Object data) {
                            VDARRemoteController.ObserverUpdateInfo info = (VDARRemoteController.ObserverUpdateInfo) data;
                            if (info.isCompleted()) {
                                syncInProgress.set(false);
                                Log.v(TAG, "Synchronization over. Synced " + info.getFetchedContexts().size() + " contents");
                            }
                        }
                    });
                }
            });
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
        return storage.getString(STORAGE_KEY_ADDITIONAL_CONTENT);
    }

    public void removeAdditionalTag() {
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT, null);
    }

    protected void onTagQrCodeScanned(String tagName) {
        if (tagName == null) {
            return;
        }
        storage.setString(STORAGE_KEY_ADDITIONAL_CONTENT, tagName);
        synchronize();
        for (VidinotiEventListener listener : eventListeners) {
            listener.onTagQrCodeScanned(tagName);
        }
    }

    public void registerListener(VidinotiEventListener listener) {
        eventListeners.add(listener);
    }

    public void unregisterListener(VidinotiEventListener listener) {
        eventListeners.remove(listener);
    }

}
