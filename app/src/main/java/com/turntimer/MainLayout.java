package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import static com.turntimer.MainActivity.displayMetricsController;

public class MainLayout extends ViewGroup
{
    Context context;
    int scaleFromMiddlePx = 1;
    private int startingChild = 1;
    private int currentChild = startingChild;
    GestureDetector gestureDetector = null;
    View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
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
        /*
        left = ScaleFromMiddle.ScaleLeft(scaleFromMiddlePx, left);
        top = ScaleFromMiddle.ScaleTop(scaleFromMiddlePx, top);
        right = ScaleFromMiddle.ScaleRight(scaleFromMiddlePx, right);
        bottom = ScaleFromMiddle.ScaleBottom(scaleFromMiddlePx, bottom);
        int height = bottom - top;
        int width = right - left;
        
        int rows = calculateRows(timerAmount, displayMetricsController.GetScreenHeight(), displayMetricsController.GetScreenWidth());
        int columns = calculateColumns(timerAmount, rows);
        
        offset.set(-1, -2, columns, rows);
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = height / rows;
            int childWidth = width / columns;
            
            tempChildRect.top = offset.top + (int) Math.floor((double) i / columns) * childHeight;
            tempChildRect.bottom = offset.bottom + tempChildRect.top + childHeight;
            tempChildRect.left = offset.left + (i % columns) * childWidth;
            tempChildRect.right = offset.right + tempChildRect.left + childWidth * ((int) Math.floor((double) i / (getChildCount() - 1)) * (i + 1) % 2 * (timerAmount % columns) + 1);
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }*/
    }
    
    
    /*
    
    private void setGestureListener()
    {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            
            @Override
            public boolean onDown(MotionEvent e)
            {
                return true;
            }
            
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                boolean result = false;
                try
                {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD && Math.abs(diffX) < Math.abs(diffY))
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
                    AnimationSet animationSet;
                    
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutleft));
                    getChildAt(currentChild).startAnimation(animationSet);
                    
                    currentChild++;
                    getChildAt(currentChild).setVisibility(View.VISIBLE);
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinleft));
                    getChildAt(currentChild).startAnimation(animationSet);
                    getChildAt(currentChild - 1).setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(context, "arrived at left", Toast.LENGTH_SHORT).show();
                }
            }
            
            void onSwipeRight()
            {
                if (currentChild > 0)
                {
                    AnimationSet animationSet;
                    
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeoutright));
                    getChildAt(currentChild).startAnimation(animationSet);
                    getChildAt(currentChild).setVisibility(View.GONE);
                    
                    currentChild--;
                    getChildAt(currentChild).setVisibility(View.VISIBLE);
                    animationSet = new AnimationSet(false);
                    animationSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeinright));
                    getChildAt(currentChild).startAnimation(animationSet);
                }
                else
                {
                    Toast.makeText(context, "arrived at right", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    protected static class ScaleFromMiddle
    {
        private static int ScaleLeft(int scaleFromMiddlePx, int left)
        {
            return left - scaleFromMiddlePx;
        }
        
        private static int ScaleTop(int scaleFromMiddlePx, int top)
        {
            return top - scaleFromMiddlePx;
        }
        
        private static int ScaleRight(int scaleFromMiddlePx, int right)
        {
            return right + 2 * scaleFromMiddlePx;
        }
        
        private static int ScaleBottom(int scaleFromMiddlePx, int bottom)
        {
            return bottom + 2 * scaleFromMiddlePx;
        }
    }*/
}