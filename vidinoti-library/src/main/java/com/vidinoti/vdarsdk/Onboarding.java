package com.vidinoti.vdarsdk;


import android.content.Context;

public final class Onboarding {

    private static final String STORAGE_KEY = "com.vidinoti.vdarsdk.ONBOARDING_VERSION";

    private Onboarding() {
    }

    public static void openWhenRequired(Context context, int onboardingVersion, String url, int titleResId) {
        VidinotiStorage storage = new VidinotiStorage(context);
        if (storage.getInt(STORAGE_KEY) < onboardingVersion) {
            storage.setInt(STORAGE_KEY, onboardingVersion);
            WebActivity.open(context, url, titleResId);
        }
    }
}
