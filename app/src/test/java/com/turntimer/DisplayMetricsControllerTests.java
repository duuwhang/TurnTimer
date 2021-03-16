package com.turntimer;

import android.util.DisplayMetrics;
import android.view.Display;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
    
    //nested class
    private static Answer<Void> reverseMsg() {
        return new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        };
    }
    @Mock
    DisplayMetrics displayMetricsMock = new DisplayMetrics();
    @Test
    public void getScreenHeight()
    {
        int height = 1000;
        DisplayMetricsController displayMetricsController = new DisplayMetricsController(displayMock, 120);
        displayMetricsMock.heightPixels = height;/*
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
                displayMetrics.setTo(displayMetricsMock);
                return null;
            }
        }).when(displayMock).getMetrics(any(DisplayMetrics.class));
        */
        //when(displayMock.getMetrics(any(DisplayMetrics.class))).then(reverseMsg());
    
        when(displayMock.getMetrics(any(DisplayMetrics.class))).thenAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
            DisplayMetrics displayMetrics = (DisplayMetrics) invocation.getArguments()[0];
            displayMetrics.setTo(displayMetricsMock);
            return null;
        }});
        
        
        Assert.assertEquals(height, displayMetricsController.getScreenHeight());
    }
    
    @Test
    public void getScreenWidth()
    {
    }
}
