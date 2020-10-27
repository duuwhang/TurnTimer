package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.turntimer.MainActivity;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.timers.TimerParentLayout;

public class SettingsPauseLayout extends BaseLayout
{
    private final TextView timersPausedText;
    private final Button resetButton;
    private final Rect tempChildRect = new Rect();
    
    public SettingsPauseLayout(final Context context)
    {
        super(context);
        
        timersPausedText = new TextView(context);
        this.addView(timersPausedText);
        
        resetButton = new Button(context);
        this.addView(resetButton);
    }
    
    @Override
    public void init()
    {
        timersPausedText.setText("Timers Paused");
        timersPausedText.setTextSize(50);
        
        resetButton.setText("Reset Timers");
        resetButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainLayout mainLayout = (MainActivity.getInstance()).getLayout();
                TimerParentLayout timerParentLayout = mainLayout.getTimerParentLayout();
                timerParentLayout.resetTimers();
            }
        });
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            
            tempChildRect.left = width / 2 - childWidth / 2;
            tempChildRect.top = (i + 1) * height / (getChildCount() + 1) - childHeight / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}
