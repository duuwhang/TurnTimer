package com.turntimer;

import android.util.DisplayMetrics;
import android.view.Display;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

public class DisplayMetricsControllerTests
{
    @Mock
    Display displayMock;
    
    @Test
    public void dpToPx_shouldConvertCorrectly()
    {
        int dp = 240;
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(displayMock, 120);
        Assert.assertEquals(180, displayMetricsController.dpToPx(dp));
        
        displayMetricsController = new DisplayMetricsController(displayMock, 240);
        Assert.assertEquals(360, displayMetricsController.dpToPx(dp));
    }
    
    @Test
    public void getScreenHeight_shouldReturnActualHeight()
    {
        int height = 1000;
        initMocks(this);
        doAnswer((Answer<Void>) invocation ->
        {
            DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
            displayMetrics.heightPixels = height;
            return null;
        }).when(displayMock).getRealMetrics(any(DisplayMetrics.class));
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(displayMock, 120);
        
        Assert.assertEquals(height, displayMetricsController.getScreenHeight());
    }
    
    @Test
    public void getScreenWidth_shouldReturnActualWidth()
    {
        int width = 1000;
        initMocks(this);
        doAnswer((Answer<Void>) invocation ->
        {
            DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
            displayMetrics.widthPixels = width;
            return null;
        }).when(displayMock).getRealMetrics(any(DisplayMetrics.class));
    
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(displayMock, 120);
    
        Assert.assertEquals(width, displayMetricsController.getScreenWidth());
    }
}
