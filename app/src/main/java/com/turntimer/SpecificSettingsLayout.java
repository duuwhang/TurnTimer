package com.turntimer;

import android.content.Context;
import android.graphics.Rect;

public class SpecificSettingsLayout extends BaseLayout
{
    AmountSettingLayout amountSettingLayout;
    CountdownSettingLayout countdownSettingLayout;
    StopwatchSettingLayout stopwatchSettingLayout;
    private Rect tempChildRect = new Rect();
    
    public SpecificSettingsLayout(Context context)
    {
        super(context);
    
        amountSettingLayout = new AmountSettingLayout(context);
        this.addView(amountSettingLayout);
        
        countdownSettingLayout = new CountdownSettingLayout(context);
        this.addView(countdownSettingLayout);
        
        stopwatchSettingLayout = new StopwatchSettingLayout(context);
        this.addView(stopwatchSettingLayout);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.left = left;
            tempChildRect.top = top + i * height / getChildCount();
            tempChildRect.right = tempChildRect.left + width;
            tempChildRect.bottom = tempChildRect.top + height / getChildCount();
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}