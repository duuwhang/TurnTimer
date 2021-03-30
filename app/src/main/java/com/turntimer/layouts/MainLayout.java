package com.turntimer.layouts;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import com.turntimer.R;
import com.turntimer.layouts.settings.SettingsParentLayout;
import com.turntimer.layouts.timers.TimersParentLayout;

public class MainLayout extends BaseLayout
{
    private int startingChild = 0;
    private int currentChild = startingChild;
    private boolean interruptClick = true;
    private SettingsParentLayout settingsParentLayout;
    private TimersParentLayout timersParentLayout;
    private GestureDetector gestureDetector = null;
    private View.OnTouchListener touchListener = new View.OnTouchListener()
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
        
        settingsParentLayout = new SettingsParentLayout(context);
        this.addView(settingsParentLayout);
        
        timersParentLayout = new TimersParentLayout(context);
        this.addView(timersParentLayout);
    }
    
    @Override
    public void init()
    {
        if (startingChild < 0 || startingChild >= getChildCount())
        {
            startingChild = 0;
        }
        currentChild = startingChild;
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).setOnTouchListener(touchListener);
            getChildAt(i).setVisibility(View.INVISIBLE);
        }
        getChildAt(startingChild).setVisibility(View.VISIBLE);
        setGestureListener();
    }
    
    public SettingsParentLayout getSettingsParentLayout()
    {
        return settingsParentLayout;
    }
    
    public TimersParentLayout getTimersParentLayout()
    {
        return timersParentLayout;
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
                            onSwipeRight();
                        }
                        else
                        {
                            onSwipeLeft();
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
                if (currentChild < getChildCount() - 1)
                {
                    AnimationSet animation = new AnimationSet(false);
                    final View child1 = getChildAt(currentChild);
                    final View child2 = getChildAt(currentChild + 1);
                    
                    animation.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutleft));
                    animation.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                            child1.setVisibility(View.INVISIBLE);
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child2.setVisibility(View.VISIBLE);
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    child1.startAnimation(animation);
                    
                    currentChild++;
                }
            }
            
            void onSwipeRight()
            {
                if (currentChild > 0)
                {
                    AnimationSet animation = new AnimationSet(false);
                    final View child1 = getChildAt(currentChild);
                    final View child2 = getChildAt(currentChild - 1);
                    
                    animation.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutright));
                    animation.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                            child1.setVisibility(View.INVISIBLE);
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child2.setVisibility(View.VISIBLE);
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    child1.startAnimation(animation);
                    
                    currentChild--;
                }
            }
        });
    }
}
