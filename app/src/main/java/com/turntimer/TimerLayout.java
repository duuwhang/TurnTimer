package com.turntimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerLayout extends ViewGroup
{
    Context context;
    EditText textView;
    TextView timerView;
    CountDownTimer countDownTimer;
    int timeMillis = 300000;
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
        
        
        /*
        Date date = Calendar.getInstance().getTime();
        Calendar date2 = Calendar.getInstance();
        activityController.Debug("" + date2.get(Calendar.MINUTE) + ":" + date.toString());
        
         */
        
        /*
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                Date date = Calendar.getInstance().getTime();
                activityController.Debug("" + date.getTime() + ":" + date.toString());
                /*timerView.setText(""+ date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND));
            }
        }, 1000, 500);*/
    }
    
    public void startCountdown()
    {
        countDownTimer = new CountDownTimer(timeMillis, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timerView.setText(String.format("%d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
                timeMillis = (int) millisUntilFinished;
            }
            
            @Override
            public void onFinish()
            {
            
            }
        };
        countDownTimer.start();
    }
    
    public void stopCountdown()
    {
        countDownTimer.cancel();
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
        /*
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        timerView.setText("" + LocalTime.of(10, 00));
        */
        
        int height = bottom - top;
        int width = right - left;
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            tempChildRect.top = (i + 1) * height / (getChildCount() + 1) - childHeight / 2;
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