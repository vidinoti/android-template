package com.vidinoti.app;

import android.app.Application;

import com.vidinoti.vdarsdk.VidinotiAR;
import com.vidinoti.vdarsdk.VidinotiAROptions;
import com.vidinoti.vdarsdk.VidinotiLanguage;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String licenseKey = "qv8db1pcnzc444ysnqtj";
        // TODO Configure me
        VidinotiAROptions options = new VidinotiAROptions.Builder(licenseKey)
                .setCodeRecognitionEnabled(true)
                .setSynchronizationMode(VidinotiAROptions.SynchronizationMode.LANGUAGE_ONLY)
                .setMultilingualEnabled(true)
                .setDefaultLanguage(VidinotiLanguage.EN)
                .setSupportedLanguages(VidinotiLanguage.EN, VidinotiLanguage.FR)
                .build();
        VidinotiAR.init(this, options).synchronize();
    }
}
