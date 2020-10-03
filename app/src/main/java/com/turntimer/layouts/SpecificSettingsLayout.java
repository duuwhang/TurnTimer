package com.turntimer.layouts;

import android.content.Context;
import android.graphics.Rect;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.turntimer.MainActivity;
import com.turntimer.layouts.settings.Setting;

public class SpecificSettingsLayout extends BaseLayout
{
    private SettingsAmountLayout settingsAmountLayout;
    private SettingsCountdownLayout settingsCountdownLayout;
    private SettingsStopwatchLayout settingsStopwatchLayout;
    private Rect tempChildRect = new Rect();
    
    public SpecificSettingsLayout(Context context)
    {
        super(context);

        Setting timerAmount = new Setting("Timer Amount", new TextView(context));
        Setting countdownMode = new Setting("Countdown Mode",
                new CheckBox(context),
                new EditText(context),
                new Spinner(context));
        Setting stopwatchMode = new Setting("Stopwatch Mode", new CheckBox(context));

        this.addView(timerAmount);
        this.addView(countdownMode);
        this.addView(stopwatchMode);

        /*
        settingsAmountLayout = new SettingsAmountLayout(context);
        this.addView(settingsAmountLayout);
        
        settingsCountdownLayout = new SettingsCountdownLayout(context);
        this.addView(settingsCountdownLayout);
        
        settingsStopwatchLayout = new SettingsStopwatchLayout(context);
        this.addView(settingsStopwatchLayout);
        */
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
    
    public SettingsStopwatchLayout GetStopwatchLayout()
    {
        return settingsStopwatchLayout;
    }
    
    public SettingsCountdownLayout GetCountdownLayout()
    {
        return settingsCountdownLayout;
    }
}