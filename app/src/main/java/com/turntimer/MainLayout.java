package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.dpToPx;
import static com.turntimer.MainActivity.screenWidth;

public class MainLayout extends ViewGroup
{
    Context context;
    int timerAmount = 2;
    int offsetY;
    float offsetYDp = 0;//-2.5f;
    private Rect tempChildRect = new Rect();
    
    public MainLayout(Context context)
    {
        super(context);
        this.context = context;
        init();
    }
    
    public MainLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        init();
    }
    
    public MainLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }
    
    private void init()
    {
        offsetY = dpToPx(context, offsetYDp);
        updateTimerAmount(timerAmount);
    }
    
    @Override
    public boolean shouldDelayChildPressedState()
    {
        return false;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int maxWidth = Math.max(screenWidth, getSuggestedMinimumWidth());
        int maxHeight = Math.max(maxWidth, getSuggestedMinimumHeight());
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.top = top + offsetY + i * getHeight() / timerAmount;
            tempChildRect.bottom = bottom + offsetY - (getChildCount() - 1 - i) * getHeight() / timerAmount;
            tempChildRect.left = left;
            tempChildRect.right = right;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    public void updateTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
        this.removeAllViewsInLayout();
        //this.layout(0, 0, 0, 0);
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            this.addView(timerLayout);
        }
    }
}