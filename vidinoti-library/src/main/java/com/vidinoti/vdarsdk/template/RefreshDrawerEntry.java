package com.vidinoti.vdarsdk.template;

import androidx.drawerlayout.widget.DrawerLayout;

import com.vidinoti.vdarsdk.VidinotiAR;

public class RefreshDrawerEntry implements DrawerEntry {
    @Override
    public void onClick(DrawerLayout drawerLayout) {
        drawerLayout.closeDrawers();
        VidinotiAR.getInstance().synchronize();
    }
}
