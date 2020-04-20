package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import static com.turntimer.MainActivity.DpToPx;
import static com.turntimer.MainActivity.screenWidth;

public class TimerLayout extends ViewGroup
{
    Context context;
    private Rect tempChildRect = new Rect();
    float outlineWidthDp = 0.5f;
    EditText textView;
    TextView timerView;
    
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
        gradientDrawable.setStroke(DpToPx(context, outlineWidthDp), ContextCompat.getColor(context, R.color.colorSeparation));
        this.setBackground(gradientDrawable);
        
        textView = new EditText(context);
        this.addView(textView);
        
        timerView = new TextView(context);
        timerView.setText("gamer");
        this.addView(timerView);
    }
    
    @Override
    public boolean shouldDelayChildPressedState()
    {
        return false;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int maxWidth = Math.max(screenWidth, getSuggestedMinimumWidth());
        int maxHeight = Math.max(maxWidth, getSuggestedMinimumHeight());
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            float childRatio = (float) getChildAt(i).getMeasuredWidth() / (float) getChildAt(i).getMeasuredHeight();
            int childHeight = (int) ((float) (right - left) / childRatio);
            tempChildRect.top = top + (1 + i) * getHeight() / 3 - childHeight / 2;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            tempChildRect.left = left;
            tempChildRect.right = right;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}