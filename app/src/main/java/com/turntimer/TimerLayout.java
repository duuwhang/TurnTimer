package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerLayout extends BaseLayout
{
    private EditText editText;
    private TextView timerView;
    private CountDownTimer countDownTimer;
    private int timeMillis = 300000;
    private float outlineWidthDp = 0.8f;
    private Rect tempChildRect = new Rect();
    TimerParentLayout.timerMode mode;
    
    public TimerLayout(Context context)
    {
        super(context);
        
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(displayMetricsController.DpToPx(outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);
        
        editText = new EditText(context);
        this.addView(editText);
        
        timerView = new TextView(context);
        timerView.setText(FormatTime(timeMillis));
        this.addView(timerView);
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
            
            tempChildRect.left = width / 2 - childWidth / 2;
            tempChildRect.top = (i + 1) * height / (getChildCount() + 1) - childHeight / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    public void StartTimer()
    {
        if (countDownTimer != null)
        {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeMillis, 200)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                if (mode == TimerParentLayout.timerMode.Stopwatch)
                {
                    millisUntilFinished = (Integer.MAX_VALUE - millisUntilFinished);
                }
                timerView.setText(FormatTime((int) millisUntilFinished));
                timeMillis = (int) millisUntilFinished;
            }
            
            @Override
            public void onFinish()
            {
                timeMillis = 0;
                EndTimer();
                ((TimerParentLayout) getParent()).SwitchToNextTimer();
            }
        };
        countDownTimer.start();
    }
    
    public void StopTimer()
    {
        countDownTimer.cancel();
    }
    
    private void EndTimer()
    {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.timerend));
        animationSet.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                clearAnimation();
                setAlpha(0.5f);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
            
            }
        });
        this.startAnimation(animationSet);
    }
    
    public boolean HasEnded()
    {
        return timeMillis == 0;
    }
    
    public void SetTimerId(int id)
    {
        editText.setText("Timer " + (id + 1));
    }
    
    public void SetTime(int millis)
    {
        timeMillis = millis;
    }
    
    private String FormatTime(int millis)
    {
        return String.format("%d:%02d", millis / 60000, (millis / 1000) % 60);
    }
}