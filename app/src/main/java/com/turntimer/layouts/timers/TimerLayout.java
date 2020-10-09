package com.turntimer.layouts.timers;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.turntimer.R;
import com.turntimer.layouts.BaseLayout;
import static com.turntimer.MainActivity.displayMetricsController;

public class TimerLayout extends BaseLayout
{
    private EditText editText;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private int timeMillis = 0;
    private float outlineWidthDp = 0.8f;
    private String name = "";
    private TimerParentLayout.TimerMode timerMode;
    private Rect tempChildRect = new Rect();
    
    public TimerLayout(Context context)
    {
        super(context);
        
        editText = new EditText(context);
        this.addView(editText);
        
        textView = new TextView(context);
        this.addView(textView);
    }
    
    @Override
    public void init()
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            
            }
            
            @Override
            public void afterTextChanged(Editable editable)
            {
                name = editText.getText().toString();
            }
        });
        
        editText.setText(name);
        
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(displayMetricsController.dpToPx(outlineWidthDp), ContextCompat.getColor(this.context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);
        
        formatTime(timeMillis);
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
    
    public void startTimer()
    {
        stopTimer();
        countDownTimer = new CountDownTimer(timeMillis, 200)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                formatTime((int) millisUntilFinished);
            }
            
            @Override
            public void onFinish()
            {
                timeMillis = 0;
                endTimer();
                ((TimerParentLayout) getParent()).switchToNextTimer();
            }
        };
        countDownTimer.start();
    }
    
    public void stopTimer()
    {
        if (isRunning())
        {
            countDownTimer.cancel();
        }
    }
    
    protected boolean isRunning()
    {
        return countDownTimer != null;
    }
    
    public boolean hasEnded()
    {
        return timeMillis == 0;
    }
    
    private void endTimer()
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
    
    private void formatTime(int millis)
    {
        if (timerMode == TimerParentLayout.TimerMode.Stopwatch)
        {
            millis = (Integer.MAX_VALUE - millis);
        }
        textView.setText(String.format("%d:%02d", millis / 60000, (millis / 1000) % 60));
    }
    
    public void setTimerMode(TimerParentLayout.TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setTimeMillis(int millis)
    {
        timeMillis = millis;
        formatTime(timeMillis);
    }
    
    public int getTimeMillis()
    {
        return timeMillis;
    }
}