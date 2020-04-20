package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.DpToPx;
import static com.turntimer.MainActivity.screenWidth;

public class MainLayout extends ViewGroup
{
    Context context;
    int timerAmount = 2;
    int scaleFromMiddlePx = 1;
    private Rect tempChildRect = new Rect();
    
    public MainLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    public MainLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        Init();
    }
    
    public MainLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        UpdateTimerAmount(timerAmount);
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
        left = ScaleLeftFromMiddle(scaleFromMiddlePx, left);
        top = ScaleTopFromMiddle(scaleFromMiddlePx, top);
        right = ScaleRightFromMiddle(scaleFromMiddlePx, right);
        bottom = ScaleBottomFromMiddle(scaleFromMiddlePx, bottom);
        int height = bottom - top;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.top = top + i * height / timerAmount;
            tempChildRect.bottom = bottom - (getChildCount() - 1 - i) * height / timerAmount;
            tempChildRect.left = left;
            tempChildRect.right = right;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    public void UpdateTimerAmount(int timerAmount)
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
    
    private int ScaleLeftFromMiddle(int scaleFromMiddlePx, int left)
    {
        return left - scaleFromMiddlePx;
    }
    
    private int ScaleTopFromMiddle(int scaleFromMiddlePx, int top)
    {
        return top - scaleFromMiddlePx;
    }
    
    private int ScaleRightFromMiddle(int scaleFromMiddlePx, int right)
    {
        return right + 2 * scaleFromMiddlePx;
    }
    
    private int ScaleBottomFromMiddle(int scaleFromMiddlePx, int bottom)
    {
        return bottom + 2 * scaleFromMiddlePx;
    }
}