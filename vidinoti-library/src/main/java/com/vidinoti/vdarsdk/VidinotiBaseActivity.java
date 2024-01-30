package com.vidinoti.vdarsdk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class VidinotiBaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        VidinotiAR.getInstance().synchronizeIfNeeded();
    }

}
