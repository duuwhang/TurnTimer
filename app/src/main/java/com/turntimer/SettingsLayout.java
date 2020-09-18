package com.turntimer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.displayMetricsController;

public class SettingsLayout extends ViewGroup
{
    Context context;
    
    public SettingsLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    public SettingsLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        Init();
    }
    
    public SettingsLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        TimerLayout timer = new TimerLayout(context);
        this.addView(timer);
    }
    
    @Override
    public boolean shouldDelayChildPressedState()
    {
        return false;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int maxWidth = Math.max(displayMetricsController.getScreenWidth(), getSuggestedMinimumWidth());
        int maxHeight = Math.max(maxWidth, getSuggestedMinimumHeight());
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }
}