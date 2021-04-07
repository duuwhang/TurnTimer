package com.turntimer.layouts.settings;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingLayout extends LinearLayout
{
    protected Context context;
    private TextView settingText;
    
    public SettingLayout(Context context)
    {
        super(context);
        this.context = context;
        init("Setting");
    }
    
    public SettingLayout(Context context, String textToDisplay, View... controlElements)
    {
        super(context);
        this.context = context;
        init(textToDisplay, controlElements);
    }
    
    private void init(String textToDisplay, View... controlElements)
    {
        settingText = new TextView(context);
        settingText.setText(textToDisplay);
        settingText.setTextSize(20);
        addView(settingText);
        
        for (View view : controlElements)
        {
            addView(view);
        }
    }
    
    public View getElement(int index)
    {
        if (index > 0 && index < getChildCount())
        {
            return getChildAt(index);
        }
        return getChildAt(0);
    }
}
