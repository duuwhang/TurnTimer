package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerParentLayout extends BaseLayout
{
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
        this.setClickable(true);
        
        UpdateTimerAmount(timerAmount);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        left = ScaleFromMiddle.ScaleLeft(scaleFromMiddlePx, left);
        top = ScaleFromMiddle.ScaleTop(scaleFromMiddlePx, top);
        right = ScaleFromMiddle.ScaleRight(scaleFromMiddlePx, right);
        bottom = ScaleFromMiddle.ScaleBottom(scaleFromMiddlePx, bottom);
        int width = right - left;
        int height = bottom - top;
        
        int rows = CalculateRows(timerAmount, displayMetricsController.GetScreenHeight(), displayMetricsController.GetScreenWidth());
        int columns = CalculateColumns(timerAmount, rows);
        
        offset.set(-1, -2, columns, rows);
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = height / rows;
            int childWidth = width / columns;
            tempChildRect.setEmpty();
            
            tempChildRect.left = offset.left + (i % columns) * childWidth;
            tempChildRect.top = offset.top + i / columns * childHeight;
            
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
                    tempChildRect.left -= tempChildRect.left / childWidth * width;
                    tempChildRect.top += tempChildRect.left / childWidth * childHeight;
                }
            }
            tempChildRect.right += offset.right + tempChildRect.left + childWidth;
            tempChildRect.bottom = offset.bottom + tempChildRect.top + childHeight;
            
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
    
    public void UpdateTimerAmount(int timerCount)
    {
        this.timerAmount = timerCount;
        activeTimerId = 0;
        this.removeAllViewsInLayout();
        
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            timerLayout.SetTimerId(i);
            this.addView(timerLayout);
        }
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((TimerLayout) getChildAt(activeTimerId)).StopTimer();
                activeTimerId += 1 - (activeTimerId + 1) / timerAmount * timerAmount;
                ((TimerLayout) getChildAt(activeTimerId)).StartTimer();
            }
        });
    }
    
    public void ResetTimers()
    {
        UpdateTimerAmount(timerAmount);
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
    
    public void SetFocus(boolean focus)
    {
        this.focus = focus;
        if (focus)
        {
            this.StartTimers();
        }
        else
        {
            this.StopTimers();
        }
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