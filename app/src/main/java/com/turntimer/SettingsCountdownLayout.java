package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingsCountdownLayout extends BaseLayout
{
    private boolean focusable = true;
    private TextView textView;
    private CheckBox checkBox;
    private Rect tempChildRect = new Rect();
    
    public SettingsCountdownLayout(Context context)
    {
        super(context);
        
        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText("Countdown Mode ");
        this.addView(textView);
        
        checkBox = new CheckBox(context);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (focusable)
                {
                    SettingsStopwatchLayout stopwatchLayout = ((SpecificSettingsLayout) getParent()).GetStopwatchLayout();
                    stopwatchLayout.ToggleChecked();
                }
            }
        });
        this.addView(checkBox);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        top = 0;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            
            tempChildRect.left = left + width / 2 - childWidth / 2 + i * width / 4;
            tempChildRect.top = top + height / 2 - childHeight / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    protected void ToggleChecked()
    {
        focusable = false;
        checkBox.setChecked(!checkBox.isChecked());
        focusable = true;
    }
}