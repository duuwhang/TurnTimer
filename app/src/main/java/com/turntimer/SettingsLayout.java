package com.turntimer;

import android.content.Context;

public class SettingsLayout extends BaseLayout
{
    Context context;
    TimerPauseLayout timerPauseLayout;
    SpecificSettingsLayout specificSettingsLayout;
    
    public SettingsLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        timerPauseLayout = new TimerPauseLayout(context);
        this.addView(timerPauseLayout);
    }
}