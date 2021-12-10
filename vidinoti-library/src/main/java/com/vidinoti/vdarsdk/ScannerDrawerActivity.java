package com.vidinoti.vdarsdk;


import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.vidinoti.vdarsdk.template.DrawerEntry;

import java.util.HashMap;
import java.util.Map;

public class ScannerDrawerActivity extends VidinotiBaseActivity implements NavigationView.OnNavigationItemSelectedListener, ScannerFragmentListener {

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private FrameLayout overlayContainer;
    private View menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vidinoti_scanner_activity);

        drawerLayout = findViewById(R.id.drawer_layout);

        overlayContainer = findViewById(R.id.overlayLayout);
        int overlayLayout = getOverlayLayoutId();
        if (overlayLayout > 0) {
            LayoutInflater.from(this).inflate(overlayLayout, overlayContainer, true);
        }

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int menuId = getNavigationMenuId();
        if (menuId > 0) {
            navigationView.inflateMenu(menuId);
        }

        ((TextView) findViewById(R.id.versionTextView))
                .setText(VidinotiUtils.getAppVersionString(this));
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerEntry entry = getDrawerEntries().get(menuItem.getItemId());
        if (entry != null) {
            entry.onClick(getDrawerLayout());
            return true;
        }
        return false;
    }

    public int getNavigationMenuId() {
        return 0;
    }

    public int getOverlayLayoutId() {
        return 0;
    }

    @SuppressWarnings("unused")
    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onAnnotationsHidden() {
        runOnUiThread(() -> {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            menuButton.setVisibility(View.VISIBLE);
            overlayContainer.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onPresentAnnotations() {

        runOnUiThread(() -> {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            menuButton.setVisibility(View.GONE);
            overlayContainer.setVisibility(View.GONE);
        });
    }

    public Map<Integer, DrawerEntry> getDrawerEntries() {
        return new HashMap<>();
    }
}
