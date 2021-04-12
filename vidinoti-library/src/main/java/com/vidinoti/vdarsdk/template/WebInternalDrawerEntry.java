package com.vidinoti.vdarsdk.template;

import androidx.drawerlayout.widget.DrawerLayout;

import com.vidinoti.vdarsdk.WebActivity;

public class WebInternalDrawerEntry implements DrawerEntry {

    private final String url;
    private final int titleResId;

    public WebInternalDrawerEntry(String url) {
        this(url, 0);
    }

    public WebInternalDrawerEntry(String url, int titleResId) {
        this.url = url;
        this.titleResId = titleResId;
    }

    @Override
    public void onClick(DrawerLayout drawerLayout) {
        WebActivity.open(drawerLayout.getContext(), url, titleResId);
        drawerLayout.closeDrawers();
    }
}
