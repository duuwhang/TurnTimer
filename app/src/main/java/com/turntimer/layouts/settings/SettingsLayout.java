package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import com.turntimer.layouts.BaseLayout;

public class SettingsLayout extends BaseLayout
{
    private SettingsPauseLayout settingsPauseLayout;
    private SettingsSubLayout settingsSubLayout;
    private Rect tempChildRect = new Rect();
    
    public SettingsLayout(Context context)
    {
        super(context);
        this.setClickable(true);
        
        settingsPauseLayout = new SettingsPauseLayout(context);
        this.addView(settingsPauseLayout);
    
        settingsSubLayout = new SettingsSubLayout(context);
        this.addView(settingsSubLayout);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.left = left;
            tempChildRect.top = top + (int) (height * (0.25f * i));// + (int) ((float) height * i / 4);
            tempChildRect.right = right;
            tempChildRect.bottom = top + (int) (height * (0.25f + 0.75f * i));
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}