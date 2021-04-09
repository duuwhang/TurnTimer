package com.turntimer.layouts;

import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import com.turntimer.MainActivity;
import com.turntimer.R;
import com.turntimer.layouts.settings.SettingsParentLayout;
import com.turntimer.layouts.timers.TimersParentLayout;
import pl.droidsonroids.gif.GifImageView;

public class MainLayout extends BaseLayout
{
    private int startingChild = 0;
    private int currentChild = startingChild;
    private boolean interruptClick = true;
    private final SettingsParentLayout settingsParentLayout;
    private final TimersParentLayout timersParentLayout;
    private final GifImageView gifImageView;
    private final Rect childRect = new Rect();
    private GestureDetector gestureDetector = null;
    private final View.OnTouchListener touchListener = (view, motionEvent) ->
    {
        if (!interruptClick)
        {
            getChildAt(currentChild).performClick();
        }
        return gestureDetector.onTouchEvent(motionEvent);
    };
    
    public MainLayout(Context context)
    {
        super(context);
        
        settingsParentLayout = new SettingsParentLayout(context);
        addView(settingsParentLayout);
        
        timersParentLayout = new TimersParentLayout(context);
        addView(timersParentLayout);
        
        gifImageView = new GifImageView(context);
        gifImageView.setImageResource(R.drawable.swipe);
        addView(gifImageView);
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
            View child = getChildAt(i);
            child.setOnTouchListener(touchListener);
            child.setVisibility(View.INVISIBLE);
        }
        getChildAt(startingChild).setVisibility(View.VISIBLE);
        setGestureListener();
        if (MainActivity.getInstance().getFirstStart())
        {
            gifImageView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).layout(300, 300, right - left, bottom - top);
        }
        childRect.left = (left + right) / 2 - gifImageView.getMeasuredWidth() / 8;
        childRect.right = childRect.left + gifImageView.getMeasuredWidth() / 4;
        childRect.top = (top + bottom) * 4 / 5 - gifImageView.getMeasuredHeight() / 8;
        childRect.bottom = childRect.top + gifImageView.getMeasuredHeight() / 4;
        gifImageView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
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
                if (currentChild < getChildCount() - 2)
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
                if (MainActivity.getInstance().getFirstStart())
                {
                    gifImageView.setVisibility(View.INVISIBLE);
                    MainActivity.getInstance().setFirstStart(false);
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
