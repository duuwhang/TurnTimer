package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerLayout extends ViewGroup
{
    Context context;
    EditText textView;
    TextView timerView;
    CountDownTimer countDownTimer;
    private Rect tempChildRect = new Rect();
    int timeMillis = 300000;
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
        gradientDrawable.setStroke(displayMetricsController.dpToPx(outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);
        
        textView = new EditText(context);
        this.addView(textView);
        
        timerView = new TextView(context);
        timerView.setText(String.format("%d:%02d", 0, 0));
        this.addView(timerView);
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
        int maxWidth = Math.max(displayMetricsController.getScreenWidth(), getSuggestedMinimumWidth());
        int maxHeight = Math.max(maxWidth, getSuggestedMinimumHeight());
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
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
    
    public void setTimerId(int id)
    {
        textView.setText("Timer " + (id + 1));
    }
}