package com.turntimer;

import android.view.Display;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class DisplayMetricsControllerTests
{
    @Mock
    Display displayMock;
    
    @Test
    public void dpToPx()
    {
        int dp = 240;
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(displayMock, 120);
        Assert.assertEquals(180, displayMetricsController.dpToPx(dp));
        
        displayMetricsController = new DisplayMetricsController(displayMock, 240);
        Assert.assertEquals(360, displayMetricsController.dpToPx(dp));
    }
    
    @Test
    public void getScreenHeight()
    {
    }
    
    @Test
    public void getScreenWidth()
    {
    }
}
