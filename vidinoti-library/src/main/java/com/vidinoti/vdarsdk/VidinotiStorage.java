package com.vidinoti.vdarsdk;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class VidinotiStorage {

    private final SharedPreferences prefs;

    public VidinotiStorage(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("com.vidinoti.vdarsdk.VidinotiPrefs",
                Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public void setBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public String getString(String key) {
        return prefs.getString(key, null);
    }

    public void setString(String key, String value) {
        if (value == null) {
            prefs.edit().remove(key).apply();
        } else {
            prefs.edit().putString(key, value).apply();
        }
    }
}
