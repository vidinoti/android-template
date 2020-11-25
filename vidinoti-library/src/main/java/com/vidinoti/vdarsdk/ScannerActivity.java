package com.vidinoti.vdarsdk;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

@SuppressWarnings("unused")
public class ScannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ScannerFragmentListener {

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
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int menuId = getNavigationMenuId();
        if (menuId > 0) {
            navigationView.inflateMenu(menuId);
        }

        VidinotiAR.getInstance().handleNotification(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VidinotiAR.getInstance().synchronizeIfNeeded();
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public int getNavigationMenuId() {
        return 0;
    }

    public int getOverlayLayoutId() {
        return 0;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onAnnotationsHidden() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                menuButton.setVisibility(View.VISIBLE);
                overlayContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPresentAnnotations() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                menuButton.setVisibility(View.GONE);
                overlayContainer.setVisibility(View.GONE);
            }
        });
    }
}
