package com.turntimer;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTests
{
    @Test
    public void getInstance()
    {
        ActivityScenario.launch(MainActivity.class);
    }
}
