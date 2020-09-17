package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import static com.turntimer.MainActivity.*;

public class MainLayout extends ViewGroup
{
    Context context;
    int timerAmount = 7;
    int scaleFromMiddlePx = 1;
    private Rect offset = new Rect();
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

        int rows = calculateRows(timerAmount, displayMetricsController.GetScreenHeight(), displayMetricsController.GetScreenWidth());
        int columns = calculateColumns(timerAmount, rows);

        offset.set(-1, -2, columns, rows);

        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = height / rows;
            int childWidth = width / columns;

            tempChildRect.top = offset.top + (int) Math.floor((double) i / columns) * childHeight;
            tempChildRect.bottom = offset.bottom + tempChildRect.top + childHeight;
            tempChildRect.left = offset.left + (i % columns) * childWidth;
            tempChildRect.right = offset.right + tempChildRect.left + childWidth * ((int) Math.floor((double) i / (getChildCount() - 1)) * (i + 1) % 2 * (timerAmount%columns) + 1);

            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }

    private int calculateRows(int timerAmount, int screenHeight, int screenWidth) {
        int rows = timerAmount;
        double minRatio = Double.MAX_VALUE;

        for(int i = 0; i < timerAmount; i++)
        {
            double totalRatio = 0;

            for(int j = 0; j < timerAmount; j++)
            {
                //i + 1 = rows
                //j + 1 = timerNumber
                double height = (double)screenHeight / (i + 1);
                int columns = calculateColumns(timerAmount, i+1);
                double width = (double)screenWidth / columns;
                int timerDifference = (i+1)*columns - timerAmount;
                if (timerDifference > 0 && j >= timerAmount - timerDifference)
                {
                    width *= 3; //punish uneven layouts
                }
                totalRatio += Math.abs(1 - width / height);
            }

            if(minRatio > totalRatio){
                minRatio = totalRatio;
                rows = i + 1;
            }
        }

        return rows;
    }

    private int calculateColumns(int timerAmount, int rows) {
        return (int) Math.ceil((double) timerAmount / rows);
    }

    public void UpdateTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
        this.removeAllViewsInLayout();
        this.layout(0, 0, 0, 0);
        for (int i = 0; i < timerAmount; i++)
        {
            TimerLayout timerLayout = new TimerLayout(context);
            timerLayout.setTimerId(i);
            this.addView(timerLayout);
        }
    }

    private static class ScaleFromMiddle
    {
        private static int ScaleLeft(int scaleFromMiddlePx, int left)
        {
            return left - scaleFromMiddlePx;
        }

        private static int ScaleTop(int scaleFromMiddlePx, int top)
        {
            return top - scaleFromMiddlePx;
        }

        private static int ScaleRight(int scaleFromMiddlePx, int right)
        {
            return right + 2 * scaleFromMiddlePx;
        }

        private static int ScaleBottom(int scaleFromMiddlePx, int bottom)
        {
            return bottom + 2 * scaleFromMiddlePx;
        }
    }
}