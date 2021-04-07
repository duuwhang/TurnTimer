package com.turntimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.settings.SettingsLayout;
import com.turntimer.layouts.timers.TimerLayout;
import com.turntimer.layouts.timers.TimersParentLayout;

public class MainActivity extends AppCompatActivity
{
    public static DisplayMetricsController displayMetricsController;
    private static MainActivity activity;
    private boolean saveState;
    private boolean loading;
    private boolean firstStart;
    private MainLayout mainLayout;
    
    public MainActivity()
    {
        activity = this;
    }
    
    public static MainActivity getInstance()
    {
        return activity;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        
        displayMetricsController = new DisplayMetricsController(getWindowManager(), getResources().getDisplayMetrics().density);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.putBoolean("saveState", saveState);
        if (saveState)
        {
            TimersParentLayout timersParentLayout = getLayout().getTimersParentLayout();
            
            if (timersParentLayout.getTimerMode() == TimersParentLayout.TimerMode.Stopwatch)
            {
                editor.putInt("timerMode", 1);
            }
            else
            {
                editor.putInt("timerMode", 0);
            }
            editor.putFloat("countdownTime", timersParentLayout.getCountdownTime());
            editor.putString("countdownUnit", timersParentLayout.getTimeUnit());
            editor.putInt("activeTimerId", timersParentLayout.getActiveTimerId());
            int timerAmount = timersParentLayout.getTimerAmount();
            editor.putInt("timerAmount", timerAmount);
            for (int i = 0; i < timerAmount; i++)
            {
                TimerLayout timerLayout = (TimerLayout) timersParentLayout.getChildAt(i);
                editor.putString("timerName" + i, timerLayout.getName());
                editor.putInt("timerTime" + i, timerLayout.getTimeMillis());
            }
        }
        
        editor.apply();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (mainLayout == null)
        {
            mainLayout = new MainLayout(this);
            setContentView(mainLayout);
            
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            if (!preferences.getBoolean("notFirstStart", false))
            {
                firstStart = true;
            }
            loading = true;
            saveState = preferences.getBoolean("saveState", false);
            if (!saveState)
            {
                SharedPreferences.Editor editor = preferences.edit();
                
                editor.putBoolean("saveState", false);
                editor.putInt("timerMode", 0);
                editor.putFloat("countdownTime", 5.0f);
                editor.putString("countdownUnit", "min");
                editor.putInt("timerAmount", 4);
                editor.putInt("activeTimerId", 0);
                
                editor.apply();
            }
            
            TimersParentLayout timersParentLayout = getLayout().getTimersParentLayout();
            SettingsLayout settingsLayout = getLayout().getSettingsParentLayout().getSettingsLayout();
            
            settingsLayout.setSaveState(saveState);
            if (preferences.getInt("timerMode", 0) == 1)
            {
                timersParentLayout.setTimerMode(TimersParentLayout.TimerMode.Stopwatch);
                settingsLayout.setTimerMode(TimersParentLayout.TimerMode.Stopwatch);
            }
            else
            {
                timersParentLayout.setTimerMode(TimersParentLayout.TimerMode.Countdown);
                settingsLayout.setTimerMode(TimersParentLayout.TimerMode.Countdown);
            }
            
            timersParentLayout.setTimeUnit(preferences.getString("countdownUnit", ""));
            settingsLayout.setTimeUnit(preferences.getString("countdownUnit", ""));
            
            timersParentLayout.setCountdownTime(preferences.getFloat("countdownTime", 0.0f));
            settingsLayout.setCountdownTime(preferences.getFloat("countdownTime", 0.0f));
            
            timersParentLayout.setTimerAmount(preferences.getInt("timerAmount", 0));
            settingsLayout.setTimerAmount(preferences.getInt("timerAmount", 0));
            
            timersParentLayout.setActiveTimerId(preferences.getInt("activeTimerId", 0));
            
            callInits(getLayout());
            loading = false;
        }
    }
    
    private void callInits(BaseLayout layout)
    {
        layout.init();
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            try
            {
                BaseLayout baseLayout = (BaseLayout) layout.getChildAt(i);
                callInits(baseLayout);
            }
            catch (ClassCastException ignored)
            {
            }
        }
    }
    
    public boolean getSaveStateOption()
    {
        return saveState;
    }
    
    public void setSaveStateOption(boolean saveState)
    {
        this.saveState = saveState;
    }
    
    public boolean getLoading()
    {
        return loading;
    }
    
    public boolean getFirstStart()
    {
        return firstStart;
    }
    
    public void setFirstStart(boolean firstStart)
    {
        this.firstStart = firstStart;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notFirstStart", true);
        editor.apply();
    }
    
    public MainLayout getLayout()
    {
        return mainLayout;
    }
}
