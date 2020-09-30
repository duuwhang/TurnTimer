package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
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
    
    public TimerLayout(Context context)
    {
        super(context);
        
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(displayMetricsController.DpToPx(outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);
        
        editText = new EditText(context);
        this.addView(editText);
        
        timerView = new TextView(context);
        timerView.setText(String.format("%d:%02d", 0, 0));
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
        countDownTimer = new CountDownTimer(timeMillis, 200)
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
    
    public void StopTimer()
    {
        countDownTimer.cancel();
    }
    
    public void SetTimerId(int id)
    {
        editText.setText("Timer " + (id + 1));
    }
}