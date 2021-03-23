package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import com.turntimer.layouts.BaseLayout;

public class SettingsParentLayout extends BaseLayout
{
    private PauseLayout pauseLayout;
    private SettingsLayout settingsLayout;
    private Rect childRect = new Rect();
    
    public SettingsParentLayout(Context context)
    {
        super(context);
    
        pauseLayout = new PauseLayout(context);
        this.addView(pauseLayout);
    
        settingsLayout = new SettingsLayout(context);
        this.addView(settingsLayout);
    }
    
    @Override
    public void init()
    {
        this.setClickable(true);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            childRect.left = left;
            childRect.top = top + i * height / 4;
            childRect.right = right;
            childRect.bottom = top + (1 + 3 * i) * height / 4;
            
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
    
    public SettingsLayout getSettingsLayout()
    {
        return settingsLayout;
    }
}
