package com.turntimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerLayout extends ViewGroup
{
    Context context;
    EditText textView;
    TextView timerView;
    private Rect tempChildRect = new Rect();
    float outlineWidthDp = 0.8f;

    public TimerLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    public TimerLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
        Init();
    }
    
    public TimerLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(displayMetricsController.DpToPx(outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);

        textView = new EditText(context);
        this.addView(textView);

        timerView = new TextView(context);
        this.addView(timerView);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar date = Calendar.getInstance();
                activityController.Debug(""+ date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND));
                /*timerView.setText(""+ date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND));*/
            }
        }, 1000, 500);
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
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        timerView.setText(""+ LocalTime.of(10,00));

        int height = bottom - top;
        int width = right - left;
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            tempChildRect.top = (i+1) * height / (getChildCount() + 1) - childHeight / 2;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            tempChildRect.left = width / 2 - childWidth / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    @SuppressLint("SetTextI18n")
    public void setTimerId(int id)
    {
        textView.setText("Timer " + (id + 1));
    }
}