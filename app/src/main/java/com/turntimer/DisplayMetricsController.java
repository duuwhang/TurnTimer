package com.turntimer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayMetricsController
{
    private Context context;
    private Resources resources;
    private WindowManager windowManager;
    
    public DisplayMetricsController(Context context, Resources resources, WindowManager windowManager)
    {
        this.context = context;
        this.resources = resources;
        this.windowManager = windowManager;
    }
    
    public int DpToPx(float dp)
    {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    
    public float PxToDp(int px)
    {
        return ((float) px / context.getResources().getDisplayMetrics().density);
    }
    
    public int GetScreenHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    
    public int GetScreenWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    
    public int GetStatusBarHeight()
    {
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
    
    public int GetActionBarHeight()
    {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarHeight;
    }
    
    public int GetNavigationBarHeight()
    {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
