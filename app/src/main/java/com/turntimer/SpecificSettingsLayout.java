package com.turntimer;

import android.content.Context;
import android.graphics.Rect;

public class SpecificSettingsLayout extends BaseLayout
{
    private SettingsAmountLayout settingsAmountLayout;
    private SettingsCountdownLayout settingsCountdownLayout;
    private SettingsStopwatchLayout settingsStopwatchLayout;
    private Rect tempChildRect = new Rect();
    
    public SpecificSettingsLayout(Context context)
    {
        super(context);
        
        settingsAmountLayout = new SettingsAmountLayout(context);
        this.addView(settingsAmountLayout);
        
        settingsCountdownLayout = new SettingsCountdownLayout(context);
        this.addView(settingsCountdownLayout);
        
        settingsStopwatchLayout = new SettingsStopwatchLayout(context);
        this.addView(settingsStopwatchLayout);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.left = left;
            tempChildRect.top = top + i * height / getChildCount() - height / getChildCount();
            tempChildRect.right = tempChildRect.left + width;
            tempChildRect.bottom = tempChildRect.top + height / getChildCount();
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}