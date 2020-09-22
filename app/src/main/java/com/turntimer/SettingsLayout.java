package com.turntimer;

import android.content.Context;
import static com.turntimer.MainActivity.activityController;

public class SettingsLayout extends BaseLayout
{
    TimerPauseLayout timerPauseLayout;
    SpecificSettingsLayout specificSettingsLayout;
    
    public SettingsLayout(Context context)
    {
        super(context);
        
        timerPauseLayout = new TimerPauseLayout(context);
        this.addView(timerPauseLayout);
        
        this.setClickable(true);
    }
}