package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.turntimer.MainActivity.activityController;

public class TimerPauseLayout extends BaseLayout
{
    private TextView timersPausedText;
    private Button resetButton;
    private Rect tempChildRect = new Rect();
    
    public TimerPauseLayout(final Context context)
    {
        super(context);
        
        timersPausedText = new TextView(context);
        timersPausedText.setText("Timers Paused");
        timersPausedText.setTextSize(50);
        this.addView(timersPausedText);
        
        resetButton = new Button(context);
        resetButton.setText("Reset Timers");
        resetButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainActivity mainActivity = (MainActivity) activityController.GetActivity();
                TimerParentLayout timerParentLayout = (TimerParentLayout) mainActivity.getLayout().getChildAt(1);
                timerParentLayout.ResetTimers();
            }
        });
        this.addView(resetButton);
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