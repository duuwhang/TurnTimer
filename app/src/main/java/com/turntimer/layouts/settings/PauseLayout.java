package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.turntimer.MainActivity;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.timers.TimersParentLayout;

public class PauseLayout extends BaseLayout
{
    private TextView timersPausedText;
    private Button resetButton;
    private Rect childRect = new Rect();
    
    public PauseLayout(final Context context)
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
                TimersParentLayout timersParentLayout = mainLayout.getTimersParentLayout();
                timersParentLayout.resetTimers();
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
    
            childRect.left = width / 2 - childWidth / 2;
            childRect.top = (i + 1) * height / (getChildCount() + 1) - childHeight / 2;
            childRect.right = childRect.left + childWidth;
            childRect.bottom = childRect.top + childHeight;
            
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
}