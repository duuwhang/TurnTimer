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

@SuppressWarnings("FieldCanBeLocal")
public class TimerLayout extends BaseLayout
{
    private final EditText editText;
    private final TextView textView;
    private CountDownTimer countDownTimer;
    private int timeMillis = 0;
    private final int outlineWidthDp = 150;
    private String name = "";
    private TimersParentLayout.TimerMode timerMode;
    private final Rect childRect = new Rect();
    private GradientDrawable gradientDrawable;
    
    public TimerLayout(Context context)
    {
        super(context);
        
        editText = new EditText(context);
        addView(editText);
        
        textView = new TextView(context);
        addView(textView);
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
                name = editable.toString();
            }
        });
        editText.setText(name);
        
        textView.setTextSize(16);
        
        formatTime(timeMillis);
        
        initGradientDrawable();
        
        if (hasEnded())
        {
            endTimer();
        }
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
            
            childRect.left = width / 2 - childWidth / 2;
            childRect.top = (i + 1) * height / (getChildCount() + 1) - childHeight / 2;
            childRect.right = childRect.left + childWidth;
            childRect.bottom = childRect.top + childHeight;
            
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
    
    private void initGradientDrawable()
    {
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(displayMetricsController.dpToPx(outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        setBackground(gradientDrawable);
    }
    
    public void startTimer()
    {
        stopTimer();
        if (!hasEnded())
        {
            countDownTimer = new CountDownTimer(timeMillis, 200)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    timeMillis = (int) millisUntilFinished;
                    formatTime((int) millisUntilFinished);
                }
                
                @Override
                public void onFinish()
                {
                    timeMillis = 0;
                    endTimer();
                    ((TimersParentLayout) getParent()).switchToNextTimer();
                }
            };
            countDownTimer.start();
            gradientDrawable.setColor(ContextCompat.getColor(context, R.color.colorAccentDark));
            setBackground(gradientDrawable);
        }
    }
    
    public void stopTimer()
    {
        if (isRunning())
        {
            countDownTimer.cancel();
        }
        initGradientDrawable();
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
                /*
                clearAnimation();
                float f = getResources().getFraction(R.fraction.anim_timer_end_alpha, 1, 1);
                setAlpha(f);
                */
                setAlpha(0.3f);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
            
            }
        });
        startAnimation(animationSet);
        stopTimer();
    }
    
    private void formatTime(int millis)
    {
        if (timerMode == TimersParentLayout.TimerMode.Stopwatch)
        {
            millis = (Integer.MAX_VALUE - millis + 900);
        }
        textView.setText(String.format("%d:%02d", millis / 60000, (millis / 1000) % 60));
    }
    
    public boolean isRunning()
    {
        return countDownTimer != null;
    }
    
    public boolean hasEnded()
    {
        return timeMillis == 0;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getTimeMillis()
    {
        return timeMillis;
    }
    
    public void setTimeMillis(int millis)
    {
        timeMillis = millis;
    }
    
    public void setTimerMode(TimersParentLayout.TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
}
