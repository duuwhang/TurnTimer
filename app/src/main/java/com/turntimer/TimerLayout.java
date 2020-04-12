package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import static com.turntimer.MainActivity.screenWidth;

public class TimerLayout extends ViewGroup
{
    Context context;
    private Rect tempChildRect = new Rect();
    EditText textView;
    TextView timerView;
    
    public TimerLayout(Context context)
    {
        super(context);
        this.context = context;
        init();
    }
    
    public TimerLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        init();
    }
    
    public TimerLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }
    
    private void init()
    {
        textView = new EditText(context);
        this.addView(textView);
        timerView = new TextView(context);
        
        timerView.setText("gamer");
        this.addView(timerView);
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
            tempChildRect.top = (1 + i) * getHeight() / 3 + getChildAt(i).getMeasuredHeight() / 2;
            tempChildRect.bottom = tempChildRect.top + getChildAt(i).getMeasuredHeight();
            tempChildRect.left = left;
            tempChildRect.right = right;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}