package com.turntimer;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.core.view.MarginLayoutParamsCompat;

public class TimerLayout extends View {
    Context context;
    public TimerLayout(Context context) {
        super(context);
        this.context = context;
        Button button = new Button(context);
        button.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        button.setText("helo");
    }
}
