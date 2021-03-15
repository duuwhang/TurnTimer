package com.turntimer;

import org.junit.Assert;
import org.junit.Test;
import static com.turntimer.MainActivity.displayMetricsController;

public class MainActivityTests
{
    @Test
    public void getInstance_shouldReturnValidInstance()
    {
        MainActivity mainActivity = new MainActivity();
        
        Assert.assertNotNull(MainActivity.getInstance());
        Assert.assertEquals(MainActivity.getInstance(), mainActivity);
    }
    
    @Test
    public void onCreate()
    {
        MainActivity mainActivity = new MainActivity();
        Assert.assertNotNull(displayMetricsController);// idk
    }
    
    @Test
    public void getSaveStateOption()
    {
    }
    
    @Test
    public void setSaveStateOption()
    {
    
    }
    
    @Test
    public void getLoading()
    {
    }
    
    @Test
    public void getLayout()
    {
    }
}
