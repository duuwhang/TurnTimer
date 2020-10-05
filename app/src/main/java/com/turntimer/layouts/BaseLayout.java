package com.turntimer.layouts;

import android.content.Context;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.displayMetricsController;

public class BaseLayout extends ViewGroup
{
    protected Context context;
    
    public BaseLayout(Context context)
    {
        super(context);
        this.context = context;
    }
    
    @Override
    public boolean shouldDelayChildPressedState()
    {
        return false;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int maxWidth = Math.max(displayMetricsController.GetScreenWidth(), getSuggestedMinimumWidth());
        int maxHeight = Math.max(maxWidth, getSuggestedMinimumHeight());
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            top = 0;
            getChildAt(i).layout(left, top, right, bottom);
        }
    }
}