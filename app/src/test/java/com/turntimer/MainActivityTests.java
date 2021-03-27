package com.turntimer;

import org.junit.Assert;
import org.junit.Test;

public class MainActivityTests
{
    @Test
    public void getInstance_shouldReturnValidInstance()
    {
        MainActivity mainActivity = new MainActivity();
        
        Assert.assertNotNull(MainActivity.getInstance());
        Assert.assertEquals(MainActivity.getInstance(), mainActivity);
    }
}
