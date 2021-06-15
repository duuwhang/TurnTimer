package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;
import com.turntimer.MainActivity;
import com.turntimer.R;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.timers.TimersParentLayout;

public class PauseLayout extends BaseLayout
{
    final TextView timersPausedText;
    final Button resetButton;
    private final Rect childRect = new Rect();
    
    public PauseLayout(final Context context)
    {
        super(context);
        
        timersPausedText = new TextView(context);
        addView(timersPausedText);
        
        resetButton = new Button(context);
        addView(resetButton);
    }
    
    @Override
    public void init()
    {
        timersPausedText.setText(R.string.timers_paused);
        timersPausedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        
        resetButton.setText(R.string.reset_timers);
        resetButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        resetButton.setOnClickListener(view ->
        {
            MainLayout mainLayout = (MainActivity.getInstance()).getLayout();
            TimersParentLayout timersParentLayout = mainLayout.getTimersParentLayout();
            timersParentLayout.resetTimers();
        });
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int width = right - left;
        int height = bottom - top;
        int heightMargin = height - timersPausedText.getMeasuredHeight() - resetButton.getMeasuredHeight();
        childRect.setEmpty();
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            
            childRect.left = width / 2 - childWidth / 2;
            childRect.top = childRect.bottom + heightMargin * 2 / 5 - i * heightMargin / 5;
            childRect.right = childRect.left + childWidth;
            childRect.bottom = childRect.top + childHeight;
            
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
}
