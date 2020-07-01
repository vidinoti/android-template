package com.vidinoti.app;


import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.vidinoti.vdarsdk.ScannerActivity;

public class StartActivity extends ScannerActivity {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (menuItem.getItemId()) {
            case R.id.nav_refresh:
                getDrawerLayout().closeDrawers();
                synchronize(true);
                return true;
        }
        return false;
    }

    @Override
    public int getNavigationMenuId() {
        return R.menu.activity_main_drawer;
    }
}
