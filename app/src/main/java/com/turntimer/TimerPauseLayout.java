package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerPauseLayout extends ViewGroup
{
    Context context;
    TextView timersPausedText;
    Button resetButton;
    private Rect tempChildRect = new Rect();
    
    public TimerPauseLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    public TimerPauseLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        Init();
    }
    
    public TimerPauseLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        timersPausedText = new TextView(context);
        timersPausedText.setText("Timers Paused");
        timersPausedText.setTextSize(50);
        this.addView(timersPausedText);
        
        //timersPausedText.setOnFocusChangeListener();
        
        resetButton = new Button(context);
        resetButton.setText("Reset Timers");
        //this.addView(resetButton);
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
            getChildAt(i).layout(left, top, right, bottom);
        }
    }
}