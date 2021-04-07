package com.turntimer.layouts.timers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import com.turntimer.MainActivity;
import com.turntimer.layouts.BaseLayout;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimersParentLayout extends BaseLayout
{
    private int scaleFromMiddlePx = 5;
    private int timerAmount;
    private int activeTimerId;
    private int countdownTimeMillis;
    private float countdownTime;
    private String timeUnit;
    private final Rect childRect = new Rect();
    private TimerMode timerMode;
    
    public enum TimerMode
    {
        Countdown,
        Stopwatch
    }
    
    public TimersParentLayout(Context context)
    {
        super(context);
    }
    
    @Override
    public void init()
    {
        setClickable(true);
        setOnClickListener(view -> switchToNextTimer());
        
        resetTimers();
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        left -= scaleFromMiddlePx;
        top -= scaleFromMiddlePx;
        right += scaleFromMiddlePx;
        bottom += 2 * scaleFromMiddlePx;
        
        int height = bottom - top;
        int width = right - left;
        
        int rows = calculateRows(timerAmount, displayMetricsController.getScreenHeight(), displayMetricsController.getScreenWidth());
        int columns = calculateColumns(timerAmount, rows);
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = height / rows;
            int childWidth = width / columns;
            childRect.setEmpty();
            
            childRect.left = left + (i % columns) * childWidth;
            childRect.top = top + i / columns * childHeight;
            
            int emptyTimerSpace = rows * columns - timerAmount;
            if (columns % 2 != 0 && timerAmount - (rows - 1) * columns == 1 && i >= timerAmount - emptyTimerSpace)
            {
                if (i == timerAmount - 1)
                {
                    childRect.right += childWidth * emptyTimerSpace;
                }
            }
            else if (emptyTimerSpace > 0 && i >= timerAmount - emptyTimerSpace)
            {
                childRect.left -= (timerAmount - emptyTimerSpace - i) * childWidth;
                childWidth *= 2;
                
                if ((rows - 1) * columns > i)
                {
                    childRect.top += childRect.left / childWidth * childHeight;
                    childRect.left -= childRect.left / childWidth * width;
                }
            }
            childRect.right += childRect.left + childWidth;
            childRect.bottom = childRect.top + childHeight;
            
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
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
                if (timerDifference > 0 && j + 1 > timerAmount - timerDifference)
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
        removeAllViewsInLayout();
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
            addView(timerLayout);
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
    
    public int getActiveTimerId()
    {
        return activeTimerId;
    }
    
    public void setActiveTimerId(int activeTimerId)
    {
        this.activeTimerId = activeTimerId;
    }
    
    public int getTimerAmount()
    {
        return timerAmount;
    }
    
    public void setTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
    }
    
    public TimerMode getTimerMode()
    {
        return timerMode;
    }
    
    public void setTimerMode(TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
    
    public float getCountdownTime()
    {
        return countdownTime;
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
    
    public String getTimeUnit()
    {
        return timeUnit;
    }
    
    public void setTimeUnit(String timeUnit)
    {
        this.timeUnit = timeUnit;
    }
}
