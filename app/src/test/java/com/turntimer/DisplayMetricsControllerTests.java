package com.turntimer;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class DisplayMetricsControllerTests
{
    @Mock
    WindowManager windowManagerMock;
    @Mock
    Display displayMock;
    
    @Test
    public void dpToPx_shouldConvertCorrectly()
    {
        int dp = 16;
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(windowManagerMock, 1);
        Assert.assertEquals(16, displayMetricsController.dpToPx(dp));
        
        displayMetricsController = new DisplayMetricsController(windowManagerMock, 2.75f);
        Assert.assertEquals(44, displayMetricsController.dpToPx(dp));
    }
    
    @Test
    public void getScreenHeight_shouldReturnActualHeight()
    {
        int height = 1000;
        MockitoAnnotations.openMocks(this);
        doReturn(displayMock).when(windowManagerMock).getDefaultDisplay();
        doAnswer((Answer<Void>) invocation ->
        {
            DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
            displayMetrics.heightPixels = height;
            return null;
        }).when(displayMock).getMetrics(any(DisplayMetrics.class));
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(windowManagerMock, 120);
        
        Assert.assertEquals(height, displayMetricsController.getScreenHeight());
    }
    
    @Test
    public void getScreenWidth_shouldReturnActualWidth()
    {
        int width = 1000;
        MockitoAnnotations.openMocks(this);
        doReturn(displayMock).when(windowManagerMock).getDefaultDisplay();
        doAnswer((Answer<Void>) invocation ->
        {
            DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
            displayMetrics.widthPixels = width;
            return null;
        }).when(displayMock).getMetrics(any(DisplayMetrics.class));
        
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(windowManagerMock, 120);
        
        Assert.assertEquals(width, displayMetricsController.getScreenWidth());
    }
}
