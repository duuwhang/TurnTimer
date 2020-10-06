package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Setting extends LinearLayout
{
    protected Context context;
    private TextView settingText;
    private View[] controlElements;
    private Rect tempChildRect = new Rect();
    
    public Setting(Context context)
    {
        super(context);
        this.context = context;
        init("Setting");
    }
    
    public Setting(Context context, String textToDisplay, View... controlElements)
    {
        super(context);
        this.context = context;
        init(textToDisplay, controlElements);
    }
    
    private void init(String textToDisplay, View... controlElements)
    {
        
        this.settingText = new TextView(context);
        this.settingText.setText(textToDisplay);
        this.settingText.setTextSize(20);
        this.addView(settingText);
        
        this.controlElements = controlElements;
        for (View v : this.controlElements)
        {
            this.addView(v);
        }
    }
}