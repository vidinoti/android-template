package com.vidinoti.vdarsdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class VidinotiUtils {

    private VidinotiUtils() {
    }

    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(250);
        }
    }

    public static void openUrlInBrowser(Context context, String url) {
        if (context == null) {
            return;
        }
        Uri uri;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            uri = null;
        }
        if (uri != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(browserIntent);
        }
    }

    /**
     * Returns the app version as a formatted string with the version name and version code.
     * (e.g. v1.2.0 (13))
     *
     * @param context the app context
     * @return the app version (empty string if an error occurred)
     */
    @NonNull
    public static String getAppVersionString(@Nullable Context context) {
        if (context == null) {
            return "";
        }
        try {
            PackageInfo pInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return "v" + pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (Exception e) {
            Log.e(VidinotiUtils.class.getName(), "Error retrieving app version", e);
        }
        return "";
    }
}
