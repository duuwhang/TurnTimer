package com.turntimer;

import org.junit.Assert;
import org.junit.Test;

public class MainActivityTests
{
    @Test
    public void getInstance_shouldReturnValidInstance()
    {
        MainActivity mainActivity = new MainActivity();
    
        Assert.assertNotEquals(MainActivity.getInstance(), null);
        Assert.assertEquals(MainActivity.getInstance(), mainActivity);
    }
    
    @Test
    public void onCreate()
    {
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
