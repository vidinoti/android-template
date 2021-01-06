package com.vidinoti.app;


import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.vidinoti.vdarsdk.ScannerActivity;
import com.vidinoti.vdarsdk.VidinotiAR;

public class StartActivity extends ScannerActivity {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_refresh:
                getDrawerLayout().closeDrawers();
                VidinotiAR.getInstance().synchronize();
                return true;
            case R.id.nav_website:
                startActivity(new Intent(this, WebActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public int getNavigationMenuId() {
        return R.menu.activity_main_drawer;
    }
}
