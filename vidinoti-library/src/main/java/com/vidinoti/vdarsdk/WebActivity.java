package com.vidinoti.vdarsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "com.vidinoti.vdarsdk.WebActivity.EXTRA_URL";
    public static final String EXTRA_TITLE_ID = "com.vidinoti.vdarsdk.WebActivity.EXTRA_TITLE_ID";

    public WebActivity() {
        super(R.layout.vidinoti_web_activity);
    }

    public static void open(Context context, String url, int titleResId) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.EXTRA_URL, url);
        intent.putExtra(WebActivity.EXTRA_TITLE_ID, titleResId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int titleResId = getIntent().getIntExtra(EXTRA_TITLE_ID, 0);
        if (titleResId != 0) {
            setTitle(titleResId);
        }
        if (savedInstanceState == null) {
            String url = getIntent().getStringExtra(EXTRA_URL);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, WebFragment.newInstance(url))
                    .commit();
        }
    }
}
