package com.vidinoti.vdarsdk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class VidinotiBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VidinotiAR.getInstance().handleNotification(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        VidinotiAR.getInstance().handleNotification(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VidinotiAR.getInstance().synchronizeIfNeeded();
    }

}
