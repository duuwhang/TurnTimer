package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class SettingsLayout extends BaseLayout
{
    TimerPauseLayout timerPauseLayout;
    SpecificSettingsLayout specificSettingsLayout;
    private Rect tempChildRect = new Rect();
    
    public SettingsLayout(Context context)
    {
        super(context);
        this.setClickable(true);
        
        timerPauseLayout = new TimerPauseLayout(context);
        this.addView(timerPauseLayout);
        
        specificSettingsLayout = new SpecificSettingsLayout(context);
        this.addView(specificSettingsLayout);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        for (int i = 0; i < getChildCount(); i++)
        {
            //tempChildRect.setEmpty();
            tempChildRect.left = left;
            tempChildRect.top = top+(int)(height*(0.25f*i));// + (int) ((float) height * i / 4);
            tempChildRect.right = right;
            tempChildRect.bottom = top + (int) (height * (0.25f + 0.75f * i));
            getChildAt(i).setY(-tempChildRect.top);
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}