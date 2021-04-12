package com.vidinoti.vdarsdk.template;

import android.content.Intent;
import android.net.Uri;

import androidx.drawerlayout.widget.DrawerLayout;

public class WebExternalDrawerEntry implements DrawerEntry {

    private final String url;

    public WebExternalDrawerEntry(String url) {
        this.url = url;
    }

    @Override
    public void onClick(DrawerLayout drawerLayout) {
        Uri uri = Uri.parse(url);
        drawerLayout.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
