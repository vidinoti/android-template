package com.vidinoti.vdarsdk.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.vidinoti.vdarsdk.R;

public class VidinotiViewfinderView extends RelativeLayout {

    public VidinotiViewfinderView(Context context) {
        this(context, null);
    }

    public VidinotiViewfinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VidinotiViewfinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VidinotiViewfinderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vidinoti_viewfinder_view, this);
        updatePadding();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePadding();
    }

    private void updatePadding() {
        int hPadding = getResources().getDimensionPixelSize(R.dimen.vidinoti_scanner_overlay_horizontal_margin);
        int vPadding = getResources().getDimensionPixelSize(R.dimen.vidinoti_scanner_overlay_vertical_margin);
        setPadding(hPadding, vPadding, hPadding, vPadding);
    }
}
