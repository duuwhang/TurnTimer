package com.turntimer.layouts.timers;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import com.turntimer.layouts.BaseLayout;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerParentLayout extends BaseLayout
{
    private int timerAmount = 4;
    private int maxTimerAmount = 30;
    private int activeTimerId = 0;
    private int timerCountdownTimeMillis = 5 * 60 * 1000;
    private int scaleFromMiddlePx = 1;
    private Rect offset = new Rect();
    private Rect tempChildRect = new Rect();
    private timerMode mode;
    
    public enum timerMode
    {
        Countdown,
        Stopwatch
    }
    
    public TimerParentLayout(Context context)
    {
        super(context);
        this.setClickable(true);
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchToNextTimer();
            }
        });
        
        mode = timerMode.Countdown;
        updateTimerAmount(timerAmount);
        ChangeTimerMode(false);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        left = ScaleFromMiddle.ScaleLeft(scaleFromMiddlePx, left);
        top = ScaleFromMiddle.ScaleTop(scaleFromMiddlePx, top);
        right = ScaleFromMiddle.ScaleRight(scaleFromMiddlePx, right);
        bottom = ScaleFromMiddle.ScaleBottom(scaleFromMiddlePx, bottom);
        //offset.set(-1, -2, columns, rows);
        
        int height = bottom - top;
        int width = right - left;
        
        int rows = calculateRows(timerAmount, displayMetricsController.getScreenHeight(), displayMetricsController.getScreenWidth());
        int columns = calculateColumns(timerAmount, rows);
        
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
    
    private int calculateRows(int timerAmount, int screenHeight, int screenWidth)
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
                int columns = calculateColumns(timerAmount, i + 1);
                double height = (double) screenHeight / (i + 1);
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
    
    private int calculateColumns(int timerAmount, int rows)
    {
        return (int) Math.ceil((double) timerAmount / rows);
    }
    
    protected void switchToNextTimer()
    {
        boolean allTimersEnded = true;
        
        for (int i = 0; i < timerAmount; i++)
        {
            if (!((TimerLayout) getChildAt(i)).hasEnded())
            {
                allTimersEnded = false;
                break;
            }
        }
        
        if (!allTimersEnded)
        {
            StopTimers();
            do
            {
                activeTimerId += 1 - (activeTimerId + 1) / timerAmount * timerAmount;
            } while (((TimerLayout) getChildAt(activeTimerId)).hasEnded());
            StartTimers();
        }
    }
    
    public void updateTimerAmount(int timerCount)
    {
        this.timerAmount = timerCount;
        activeTimerId = 0;
        this.removeAllViewsInLayout();
        
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            timerLayout.setTimerId(i);
            timerLayout.setTime(timerCountdownTimeMillis);
            this.addView(timerLayout);
        }
    }
    
    public void resetTimers()
    {
        updateTimerAmount(timerAmount);
    }
    
    private void StartTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.startTimer();
    }
    
    private void StopTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.stopTimer();
    }
    
    private void SetTimerCountdownTime(int timeMillis)
    {
        timerCountdownTimeMillis = timeMillis;
        if (mode == timerMode.Countdown)
        {
            resetTimers();
        }
    }
    
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE)
        {
            StartTimers();
        }
        else
        {
            StopTimers();
        }
    }
    
    private void ChangeTimerMode(boolean toggle)
    {
        if (!toggle)
        {
            switch (mode)
            {
                case Countdown:
                    mode = timerMode.Stopwatch;
                    break;
                default:
                case Stopwatch:
                    mode = timerMode.Countdown;
                    break;
            }
        }
        
        resetTimers();
        switch (mode)
        {
            case Countdown:
                mode = timerMode.Stopwatch;
                for (int i = 0; i < timerAmount; i++)
                {
                    TimerLayout timer = (TimerLayout) getChildAt(i);
                    timer.mode = timerMode.Stopwatch;
                    timer.setTime(Integer.MAX_VALUE);
                }
                break;
            default:
            case Stopwatch:
                mode = timerMode.Countdown;
                for (int i = 0; i < timerAmount; i++)
                {
                    TimerLayout timer = (TimerLayout) getChildAt(i);
                    timer.mode = timerMode.Countdown;
                    timer.setTime(timerCountdownTimeMillis);
                }
                break;
        }
    }
    
    public void ChangeTimerMode(timerMode tMode)
    {
        if (tMode != mode)
        {
            mode = tMode;
            resetTimers();
            switch (mode)
            {
                case Countdown:
                    for (int i = 0; i < timerAmount; i++)
                    {
                        TimerLayout timer = (TimerLayout) getChildAt(i);
                        timer.mode = timerMode.Countdown;
                        timer.setTime(timerCountdownTimeMillis);
                    }
                    break;
                default:
                case Stopwatch:
                    for (int i = 0; i < timerAmount; i++)
                    {
                        TimerLayout timer = (TimerLayout) getChildAt(i);
                        timer.mode = timerMode.Stopwatch;
                        timer.setTime(Integer.MAX_VALUE);
                    }
                    break;
            }
        }
    }
    
    protected static class ScaleFromMiddle
    {
        protected static int ScaleLeft(int scaleFromMiddlePx, int left)
        {
            return left - 2 * scaleFromMiddlePx;
        }
        
        protected static int ScaleTop(int scaleFromMiddlePx, int top)
        {
            return top - 2 * scaleFromMiddlePx;
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