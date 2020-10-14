package com.turntimer.layouts.timers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import com.turntimer.MainActivity;
import com.turntimer.layouts.BaseLayout;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerParentLayout extends BaseLayout
{
    private int scaleFromMiddlePx = 1;
    private int timerAmount;
    private int activeTimerId;
    private int countdownTimeMillis;
    private float countdownTime;
    private String timeUnit;
    private Rect offset = new Rect();
    private Rect tempChildRect = new Rect();
    private TimerMode timerMode;
    
    public enum TimerMode
    {
        Countdown,
        Stopwatch
    }
    
    public TimerParentLayout(Context context)
    {
        super(context);
    }
    
    @Override
    public void init()
    {
        this.setClickable(true);
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchToNextTimer();
            }
        });
        
        resetTimers();
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        left = ScaleFromMiddle.scaleLeft(scaleFromMiddlePx, left);
        top = ScaleFromMiddle.scaleTop(scaleFromMiddlePx, top);
        right = ScaleFromMiddle.scaleRight(scaleFromMiddlePx, right);
        bottom = ScaleFromMiddle.scaleBottom(scaleFromMiddlePx, bottom);
        
        int height = bottom - top;
        int width = right - left;
        
        int rows = calculateRows(timerAmount, displayMetricsController.getScreenHeight(), displayMetricsController.getScreenWidth());
        int columns = calculateColumns(timerAmount, rows);
        
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
    
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE)
        {
            startTimers();
        }
        else
        {
            stopTimers();
        }
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
            stopTimers();
            do
            {
                activeTimerId += 1 - (activeTimerId + 1) / timerAmount * timerAmount;
            } while (((TimerLayout) getChildAt(activeTimerId)).hasEnded());
            startTimers();
        }
    }
    
    public void resetTimers()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            ((TimerLayout) getChildAt(i)).stopTimer();
        }
        this.removeAllViewsInLayout();
        if (!MainActivity.getInstance().getLoading())
        {
            activeTimerId = 0;
        }
        
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            SharedPreferences preferences = MainActivity.getInstance().getPreferences(Context.MODE_PRIVATE);
            
            switch (timerMode)
            {
                default:
                case Countdown:
                    timerLayout.setTimeMillis(countdownTimeMillis);
                    break;
                case Stopwatch:
                    timerLayout.setTimeMillis(Integer.MAX_VALUE);
                    break;
            }
            if (MainActivity.getInstance().getSaveStateOption())
            {
                timerLayout.setName(preferences.getString("timerName" + i, ""));
                if (MainActivity.getInstance().getLoading())
                {
                    timerLayout.setTimeMillis(preferences.getInt("timerTime" + i, 1));
                }
            }
            
            if (timerLayout.getName().equals(""))
            {
                timerLayout.setName("Timer " + (i + 1));
            }
            timerLayout.setTimerMode(timerMode);
            timerLayout.init();
            this.addView(timerLayout);
        }
    }
    
    private void startTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.startTimer();
    }
    
    private void stopTimers()
    {
        TimerLayout timerLayout = (TimerLayout) getChildAt(activeTimerId);
        timerLayout.stopTimer();
    }
    
    public void setActiveTimerId(int activeTimerId)
    {
        this.activeTimerId = activeTimerId;
    }
    
    public int getActiveTimerId()
    {
        return activeTimerId;
    }
    
    public void setTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
    }
    
    public int getTimerAmount()
    {
        return timerAmount;
    }
    
    public void setTimerMode(TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
    
    public TimerMode getTimerMode()
    {
        return timerMode;
    }
    
    public void setCountdownTime(float time)
    {
        this.countdownTime = time;
        
        if (timeUnit.equals("min"))
        {
            time *= 60;
        }
        countdownTimeMillis = (int) time * 1000;
    }
    
    public float getCountdownTime()
    {
        return countdownTime;
    }
    
    public void setTimeUnit(String timeUnit)
    {
        this.timeUnit = timeUnit;
    }
    
    public String getTimeUnit()
    {
        return timeUnit;
    }
    
    protected static class ScaleFromMiddle
    {
        protected static int scaleLeft(int scaleFromMiddlePx, int left)
        {
            return left - scaleFromMiddlePx;
        }
        
        protected static int scaleTop(int scaleFromMiddlePx, int top)
        {
            return top - scaleFromMiddlePx;
        }
        
        protected static int scaleRight(int scaleFromMiddlePx, int right)
        {
            return right + scaleFromMiddlePx;
        }
        
        protected static int scaleBottom(int scaleFromMiddlePx, int bottom)
        {
            return bottom + scaleFromMiddlePx;
        }
    }
}