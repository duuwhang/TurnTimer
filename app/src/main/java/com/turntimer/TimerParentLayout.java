package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerParentLayout extends ViewGroup
{
    Context context;
    boolean focus = false;
    int timerAmount = 4;
    int maxTimerAmount = 30;
    int activeTimerId = 0;
    int scaleFromMiddlePx = 1;
    private Rect offset = new Rect();
    private Rect tempChildRect = new Rect();
    
    public TimerParentLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    public TimerParentLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        Init();
    }
    
    public TimerParentLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        UpdateTimerAmount(timerAmount);
        
        this.setClickable(true);
        /*
        new CountDownTimer(1500, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
            
            }
            
            @Override
            public void onFinish()
            {
                if (timerAmount < maxTimerAmount)
                {
                    timerAmount++;
                    UpdateTimerAmount(timerAmount);
                    start();
                }
            }
        }.start();*/
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
        left = ScaleFromMiddle.ScaleLeft(scaleFromMiddlePx, left);
        top = ScaleFromMiddle.ScaleTop(scaleFromMiddlePx, top);
        right = ScaleFromMiddle.ScaleRight(scaleFromMiddlePx, right);
        bottom = ScaleFromMiddle.ScaleBottom(scaleFromMiddlePx, bottom);
        int height = bottom - top;
        int width = right - left;
        
        int rows = CalculateRows(timerAmount, displayMetricsController.GetScreenHeight(), displayMetricsController.GetScreenWidth());
        int columns = CalculateColumns(timerAmount, rows);
        
        offset.set(-1, -2, columns, rows);
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = height / rows;
            int childWidth = width / columns;
            tempChildRect.setEmpty();
            
            tempChildRect.top = offset.top + i / columns * childHeight;
            tempChildRect.left = offset.left + (i % columns) * childWidth;
            
            int timerDifference = rows * columns - timerAmount;
            if (timerAmount == 13 || timerAmount == 16) // fix double width exception
            {
                tempChildRect.right += childWidth * ((i + 1) / timerAmount * (timerAmount / rows));
            }
            else if (timerDifference > 0 && i >= timerAmount - timerDifference) // place multiple timers with double width
            {
                childWidth *= 2;
                tempChildRect.left += (timerAmount - i - timerDifference) * -0.5 * childWidth;
                
                if ((rows - 1) * columns > i)
                {
                    tempChildRect.top += tempChildRect.left / childWidth * childHeight;
                    tempChildRect.left -= tempChildRect.left / childWidth * width;
                }
            }
            tempChildRect.bottom = offset.bottom + tempChildRect.top + childHeight;
            tempChildRect.right += offset.right + tempChildRect.left + childWidth;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        } // flawed system -> hard 13, 16 exception & max amount is 30
    }
    
    private int CalculateRows(int timerAmount, int screenHeight, int screenWidth)
    {
        int rows = timerAmount;
        double minRatio = Double.MAX_VALUE;
        
        for (int i = 0; i < timerAmount; i++)
        {
            double totalRatio = 0;
            
            for (int j = 0; j < timerAmount; j++)
            {
                //i + 1 = rows
                //j + 1 = timerNumber
                double height = (double) screenHeight / (i + 1);
                int columns = CalculateColumns(timerAmount, i + 1);
                double width = (double) screenWidth / columns;
                int timerDifference = (i + 1) * columns - timerAmount;
                if (timerDifference > 0 && j >= timerAmount - timerDifference)
                {
                    width *= 2 + 1; //punish uneven layouts
                }
                totalRatio += Math.abs(1 - width / height);
            }
            
            if (minRatio > totalRatio)
            {
                minRatio = totalRatio;
                rows = i + 1;
            }
        }
        
        return rows;
    }
    
    private int CalculateColumns(int timerAmount, int rows)
    {
        return (int) Math.ceil((double) timerAmount / rows);
    }
    
    public void UpdateTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
        final int timerCount = timerAmount;
        
        activeTimerId = 0;
        this.removeAllViewsInLayout();
        this.layout(0, 0, 0, 0);
        
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            timerLayout.SetTimerId(i);
            this.addView(timerLayout);
        }
        TimerLayout timer = (TimerLayout) getChildAt(0);
        //timer.StartTimer();
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimerLayout timer = (TimerLayout) getChildAt(activeTimerId);
                timer.StopTimer();
                activeTimerId += 1 - (activeTimerId + 1) / timerCount * timerCount;
                timer = (TimerLayout) getChildAt(activeTimerId);
                timer.StartTimer();
            }
        });
    }
    
    public void StartTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.StartTimer();
    }
    
    public void StopTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.StopTimer();
    }
    
    protected static class ScaleFromMiddle
    {
        protected static int ScaleLeft(int scaleFromMiddlePx, int left)
        {
            return left - scaleFromMiddlePx;
        }
        
        protected static int ScaleTop(int scaleFromMiddlePx, int top)
        {
            return top - scaleFromMiddlePx;
        }
        
        protected static int ScaleRight(int scaleFromMiddlePx, int right)
        {
            return right + 2 * scaleFromMiddlePx;
        }
        
        protected static int ScaleBottom(int scaleFromMiddlePx, int bottom)
        {
            return bottom + 2 * scaleFromMiddlePx;
        }
    }
}