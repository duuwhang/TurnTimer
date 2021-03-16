package com.turntimer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class MainActivityTests
{
    @Test
    public void getInstance_shouldReturnValidInstance()
    {
        MainActivity mainActivity = new MainActivity();
        
        Assert.assertNotNull(MainActivity.getInstance());
        Assert.assertEquals(mainActivity, MainActivity.getInstance());
    }
    
    @Test
    public void onCreate() //idk
    {
        MainActivity mainActivity = new MainActivity();
    }
    
    @Test
    public void getSaveStateOption_shouldReturnDefaultFalse() // getter setter?
    {
        MainActivity mainActivity = new MainActivity();
        Assert.assertFalse(mainActivity.getSaveStateOption());
    }
    
    @Test
    public void setSaveStateOption_shouldSetVariable()
    {
        MainActivity mainActivity = new MainActivity();
        mainActivity.setSaveStateOption(false);
        Assert.assertFalse(mainActivity.getSaveStateOption());
        mainActivity.setSaveStateOption(true);
        Assert.assertTrue(mainActivity.getSaveStateOption());
    }
    
    @Test
    public void getLoading()
    {
    }
    
    @Mock
    private MainActivity mockFoo;
    
    @Test
    public void getLayout()
    {
    }
}
