package com.turntimer;

import android.content.Context;
import android.widget.Button;

public class SettingsCountdownLayout extends BaseLayout
{
    public SettingsCountdownLayout(Context context)
    {
        super(context);
        
        this.addView(new Button(context));
    }
}