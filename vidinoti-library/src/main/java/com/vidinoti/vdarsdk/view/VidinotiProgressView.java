package com.vidinoti.vdarsdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vidinoti.vdarsdk.R;

public class VidinotiProgressView extends LinearLayout {

    private TextView progressTextView;

    public VidinotiProgressView(Context context) {
        this(context, null);
    }

    public VidinotiProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VidinotiProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VidinotiProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vidinoti_progress_view, this);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        progressTextView = findViewById(R.id.vidinotiProgressTextView);
    }

    /**
     * Sets the progress in percent. When the progress is 100, the view disappears (gone).
     * @param progress the percentage progress (value between 0 and 100)
     */
    @SuppressLint("SetTextI18n")
    public void setProgress(int progress) {
        if (progress < 100) {
            progressTextView.setText(progress + "%");
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }
}
