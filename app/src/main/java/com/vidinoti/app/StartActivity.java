package com.vidinoti.app;


import android.os.Bundle;

import com.vidinoti.vdarsdk.Onboarding;
import com.vidinoti.vdarsdk.ScannerDrawerActivity;
import com.vidinoti.vdarsdk.template.DrawerEntry;
import com.vidinoti.vdarsdk.template.RefreshDrawerEntry;
import com.vidinoti.vdarsdk.template.WebExternalDrawerEntry;
import com.vidinoti.vdarsdk.template.WebInternalDrawerEntry;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends ScannerDrawerActivity {

    private static final String INFO_PAGE_URL = "file:///android_asset/info.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Onboarding.openWhenRequired(this, 1, INFO_PAGE_URL, R.string.menu_info);
    }

    @Override
    public int getNavigationMenuId() {
        return R.menu.activity_main_drawer;
    }

    @Override
    public Map<Integer, DrawerEntry> getDrawerEntries() {
        Map<Integer, DrawerEntry> menu = new HashMap<>();
        menu.put(R.id.nav_refresh, new RefreshDrawerEntry());
        menu.put(R.id.nav_website, new WebExternalDrawerEntry("https://www.vidinoti.com"));
        menu.put(R.id.nav_info, new WebInternalDrawerEntry(INFO_PAGE_URL, R.string.menu_info));
        return menu;
    }
}
