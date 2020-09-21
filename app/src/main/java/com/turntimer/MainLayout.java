package com.turntimer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class MainLayout extends ViewGroup
{
    Context context;
    private int startingChild = 1;
    private int currentChild = startingChild;
    boolean interruptClick = true;
    GestureDetector gestureDetector = null;
    View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            if (!interruptClick)
            {
                getChildAt(currentChild).performClick();
            }
            return gestureDetector.onTouchEvent(motionEvent);
        }
    };
    
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
        this.setClickable(true);
        
        SettingsLayout settingsLayout = new SettingsLayout(context);
        this.addView(settingsLayout);
        
        TimerParentLayout timerParentLayout = new TimerParentLayout(context);
        this.addView(timerParentLayout);
        
        if (startingChild < 0 || startingChild >= getChildCount())
        {
            startingChild = 0;
            currentChild = startingChild;
        }
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).setOnTouchListener(touchListener);
            getChildAt(i).setVisibility(View.GONE);
        }
        getChildAt(startingChild).setVisibility(View.VISIBLE);
        
        setGestureListener();
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
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }
    
    private void setGestureListener()
    {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            
            @Override
            public boolean onDown(MotionEvent e)
            {
                return false;
            }
            
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                boolean result = false;
                try
                {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && Math.abs(diffX) > Math.abs(diffY))
                    {
                        if (diffX > 0)
                        {
                            onSwipeLeft();
                        }
                        else
                        {
                            onSwipeRight();
                        }
                        result = true;
                        interruptClick = true;
                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
                return result;
            }
            
            void onSwipeLeft()
            {
                if (currentChild > 0)
                {
                    AnimationSet animationSet = new AnimationSet(false);
                    
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutright));
                    getChildAt(currentChild).startAnimation(animationSet);
                    
                    while (!animationSet.hasEnded())
                    {
                        //animationSet.notify();
                        animationSet.reset();
                        animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinright));
                        getChildAt(currentChild - 1).startAnimation(animationSet);
                        
                        getChildAt(currentChild).setVisibility(View.GONE);
                        getChildAt(currentChild - 1).setVisibility(View.VISIBLE);
                        currentChild--;
                        getChildAt(currentChild).setOnTouchListener(touchListener);
                    }
                }
                else
                {
                    activityController.debug("arrived at left");
                }
            }
            
            void onSwipeRight()
            {
                if (currentChild < getChildCount() - 1)
                {
                    AnimationSet animationSet = new AnimationSet(false);
                    
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutleft));
                    getChildAt(currentChild).startAnimation(animationSet);
                    
                    currentChild++;
                    getChildAt(currentChild).setVisibility(View.VISIBLE);
                    animationSet.reset();
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinleft));
                    getChildAt(currentChild).startAnimation(animationSet);
                    getChildAt(currentChild - 1).setVisibility(View.GONE);
                }
                else
                {
                    activityController.debug("arrived at right");
                }
            }
        });
    }
}