package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.TextView;

public class SpecificSettingsLayout extends BaseLayout
{
    private Rect tempChildRect = new Rect();
    
    public SpecificSettingsLayout(Context context)
    {
        super(context);
        Button resetButton = new Button(context);
        resetButton.setText("Reset Timers");
        this.addView(resetButton);
    }
}