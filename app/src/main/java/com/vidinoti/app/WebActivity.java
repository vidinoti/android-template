package com.vidinoti.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vidinoti.vdarsdk.WebFragment;

public class WebActivity extends AppCompatActivity {

    public WebActivity() {
        super(R.layout.web_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            WebFragment webFragment = WebFragment.newInstance("https://www.vidinoti.com");
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, webFragment)
                    .commit();
        }
    }

}
