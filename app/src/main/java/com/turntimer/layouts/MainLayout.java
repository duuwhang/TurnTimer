package com.turntimer.layouts;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import com.turntimer.R;
import com.turntimer.layouts.settings.SettingsLayout;
import com.turntimer.layouts.timers.TimerParentLayout;

public class MainLayout extends BaseLayout
{
    private int startingChild = 0;
    private int currentChild = startingChild;
    private boolean interruptClick = true;
    private SettingsLayout settingsLayout;
    private TimerParentLayout timerParentLayout;
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
        
        settingsLayout = new SettingsLayout(context);
        this.addView(settingsLayout);
    
        timerParentLayout = new TimerParentLayout(context);
        this.addView(timerParentLayout);
        
        if (startingChild < 0 || startingChild >= getChildCount())
        {
            startingChild = 0;
            currentChild = startingChild;
        }
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).setOnTouchListener(touchListener);
            getChildAt(i).setVisibility(View.INVISIBLE);
        }
        getChildAt(startingChild).setVisibility(View.VISIBLE);
        setGestureListener();
    }
    
    public TimerParentLayout getTimerParentLayout()
    {
        return timerParentLayout;
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
            
            void onSwipeLeft() // experimental
            {
                if (currentChild > 0)
                {
                    AnimationSet animationSet = new AnimationSet(false);
                    
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutright));
                    final View child1 = getChildAt(currentChild);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child1.setVisibility(View.INVISIBLE);
                            child1.clearAnimation();
                            child1.setX(child1.getWidth());
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    child1.startAnimation(animationSet);
                    
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinright));
                    final View child2 = getChildAt(currentChild - 1);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                            child2.setX(-child1.getWidth());
                            child2.setVisibility(View.VISIBLE);
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child2.setX(0);
                            child2.clearAnimation();
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    child2.startAnimation(animationSet);
                    
                    currentChild--;
                }
            }
            
            void onSwipeRight() // experimental
            {
                if (currentChild < getChildCount() - 1)
                {
                    
                    AnimationSet animationSet = new AnimationSet(false);
                    
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutleft));
                    final View child1 = getChildAt(currentChild);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child1.setX(-child1.getWidth());
                            child1.setVisibility(View.INVISIBLE);
                            child1.clearAnimation();
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    child1.startAnimation(animationSet);
                    
                    animationSet = new AnimationSet(false);
                    final View child2 = getChildAt(currentChild + 1);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                            child2.setX(child2.getWidth());
                            child2.setVisibility(View.VISIBLE);
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            child2.setX(0);
                            child2.clearAnimation();
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinleft));
                    child2.startAnimation(animationSet);
                    
                    currentChild++;
                }
            }
        });
    }
}