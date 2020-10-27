package com.turntimer;

import android.util.DisplayMetrics;
import android.view.Display;

public class DisplayMetricsController
{
    private final float pixelDensity;
    private final Display display;
    
    public DisplayMetricsController(Display display, float pixelDensity)
    {
        this.pixelDensity = pixelDensity;
        this.display = display;
    }
    
    public int dpToPx(float dp)
    {
        return (int) (dp * pixelDensity);
    }
    
    public int getScreenHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    
    public int getScreenWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
