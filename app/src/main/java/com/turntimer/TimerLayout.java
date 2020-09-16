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
import static com.turntimer.MainActivity.*;

public class TimerLayout extends ViewGroup
{
    Context context;
    private Rect tempChildRect = new Rect();
    float outlineWidthDp = 0.8f;
    TextView timerView;
    EditText textView;
    
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
        
        timerView = new TextView(context);
        this.addView(timerView);
        
        textView = new EditText(context);
        this.addView(textView);
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
        top = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            tempChildRect.top = top + (1 + i) * getHeight() / 3 - childHeight / 2;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            tempChildRect.left = left + getChildAt(i).getWidth() / 2;
            tempChildRect.right = right - getChildAt(i).getWidth() / 2;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    @SuppressLint("SetTextI18n")
    public void setTimerId(int id)
    {
        timerView.setText("Timer " + (id + 1));
    }
}