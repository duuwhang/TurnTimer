package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import com.turntimer.layouts.BaseLayout;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsParentLayout extends BaseLayout
{
    private final PauseLayout pauseLayout;
    private final SettingsLayout settingsLayout;
    private final Rect childRect = new Rect();
    
    public SettingsParentLayout(Context context)
    {
        super(context);
        
        pauseLayout = new PauseLayout(context);
        addView(pauseLayout);
        
        settingsLayout = new SettingsLayout(context);
        addView(settingsLayout);
    }
    
    @Override
    public void init()
    {
        setClickable(true);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int width = right - left;
        int height = bottom - top;
        childRect.left = 0;
        childRect.right = width;
        
        childRect.top = 0;
        childRect.bottom = Integer.max(height / 4, pauseLayout.timersPausedText.getMeasuredHeight() + pauseLayout.resetButton.getMeasuredHeight());
        pauseLayout.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        
        childRect.top = childRect.bottom;
        childRect.bottom = height;
        settingsLayout.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
    }
    
    public SettingsLayout getSettingsLayout()
    {
        return settingsLayout;
    }
}
