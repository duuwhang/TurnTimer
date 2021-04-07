package com.turntimer;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class DisplayMetricsController
{
    private final float pixelDensity;
    private final WindowManager windowManager;
    
    public DisplayMetricsController(WindowManager windowManager, float pixelDensity)
    {
        this.pixelDensity = pixelDensity;
        this.windowManager = windowManager;
    }
    
    public int dpToPx(float dp)
    {
        return (int) (dp * pixelDensity) / 160;
    }
    
    public int getScreenHeight()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            return windowMetrics.getBounds().height();
        }
        else
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }
    
    public int getScreenWidth()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            return windowMetrics.getBounds().width();
        }
        else
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }
}
