package com.turntimer;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import static com.turntimer.MainActivity.activityController;

public class MainLayout extends BaseLayout
{
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
    //static TimerParentLayout timerParentLayout;
    
    public MainLayout(Context context)
    {
        super(context);
    
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
            getChildAt(i).setVisibility(View.INVISIBLE);
        }
        getChildAt(startingChild).setVisibility(View.VISIBLE);
        ((BaseLayout) getChildAt(startingChild)).SetFocus(true);
    
        SetGestureListener();
    }
    
    private void SetGestureListener()
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
                            ((BaseLayout) child1).SetFocus(false);
                            child1.setVisibility(View.INVISIBLE);
                            child1.clearAnimation();
                            child1.setX(child1.getWidth());
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    getChildAt(currentChild).startAnimation(animationSet);
                    
                    //animationSet.reset();
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinright));
                    final View child2 = getChildAt(currentChild - 1);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            ((BaseLayout) child2).SetFocus(true);
                            child2.setVisibility(View.VISIBLE);
                            child2.clearAnimation();
                            child2.setX(0);
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    getChildAt(currentChild - 1).startAnimation(animationSet);
                    
                    currentChild--;
                }
                else
                {
                    activityController.Debug("arrived at left");
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
                            ((BaseLayout) child1).SetFocus(false);
                            child1.setVisibility(View.INVISIBLE);
                            child1.clearAnimation();
                            child1.setX(-child1.getWidth());
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    getChildAt(currentChild).startAnimation(animationSet);
                    
                    animationSet = new AnimationSet(false);
                    final View child2 = getChildAt(currentChild + 1);
                    animationSet.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            ((BaseLayout) child2).SetFocus(true);
                            child2.setVisibility(View.VISIBLE);
                            child2.clearAnimation();
                            child2.setX(0);
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        
                        }
                    });
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinleft));
                    getChildAt(currentChild + 1).startAnimation(animationSet);
                    
                    currentChild++;
                }
                else
                {
                    activityController.Debug("arrived at right");
                }
            }
        });
    }
}