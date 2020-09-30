package com.turntimer;

import android.content.Context;
import android.widget.Button;

public class SettingsStopwatchLayout extends BaseLayout
{
    public SettingsStopwatchLayout(Context context)
    {
        super(context);
        
        this.addView(new Button(context));
    }
}