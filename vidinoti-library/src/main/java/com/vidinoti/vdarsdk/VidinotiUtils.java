package com.vidinoti.vdarsdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

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
}
