package com.turntimer;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.timers.TimerParentLayout;

public class MainActivity extends AppCompatActivity
{
    private boolean saveState;
    private MainLayout mainLayout;
    private static MainActivity activity;
    public static DisplayMetricsController displayMetricsController;
    
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
        
        Resources resources = getResources();
        WindowManager windowManager = getWindowManager();
        displayMetricsController = new DisplayMetricsController(windowManager.getDefaultDisplay(), resources.getDisplayMetrics().density);
        
        initialiseLayout();
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.putBoolean("saveState", saveState);
        if (saveState)
        {
            TimerParentLayout timerParentLayout = getLayout().getTimerParentLayout();
            
            if (timerParentLayout.getTimerMode() == TimerParentLayout.TimerMode.Countdown)
            {
                editor.putBoolean("countdownMode", true);
                editor.putBoolean("stopwatchMode", false);
            }
            else
            {
                editor.putBoolean("countdownMode", false);
                editor.putBoolean("stopwatchMode", true);
            }
            editor.putInt("timerAmount", 5);
        }
        
        editor.apply();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        
        saveState = preferences.getBoolean("saveState", false);
        if (saveState)
        {
            TimerParentLayout timerParentLayout = getLayout().getTimerParentLayout();
            
            if (preferences.getBoolean("countdownMode", false))
            {
                timerParentLayout.changeTimerMode(TimerParentLayout.TimerMode.Countdown);
            }
            else
            {
                timerParentLayout.changeTimerMode(TimerParentLayout.TimerMode.Stopwatch);
            }
        }
        else //default
        {
            SharedPreferences.Editor editor = preferences.edit();
            
            editor.putBoolean("saveState", false);
            editor.putBoolean("countdownMode", true);
            editor.putBoolean("stopwatchMode", false);
            editor.putFloat("countdownTime", 5.0f);
            editor.putString("countdownUnit", "min");
            editor.putInt("timerAmount", 4);
            for (int i = 0; i < 4; i++)
            {
                editor.putString("timerName" + i, "Timer " + (i + 1));
                editor.putInt("timerTime" + i, 5 * 60 * 100);
            }
            
            editor.apply();
        }
        getLayout().getTimerParentLayout().Init();
    }
    
    private void initialiseLayout()
    {
        mainLayout = new MainLayout(this);
        setContentView(mainLayout);
    }
    
    public MainLayout getLayout()
    {
        return mainLayout;
    }
    
    public void setSaveStateOption(boolean saveState)
    {
        this.saveState = saveState;
    }
    
    public void setPreference(String key, boolean value)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.putBoolean(key, value);
        
        editor.apply();
    }
    
    public void setPreference(String key, int value)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.putInt(key, value);
        
        editor.apply();
    }
    
    public void setPreference(String key, String value)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.putString(key, value);
        
        editor.apply();
    }
    
    public boolean getPreference(String key, boolean defaultValue)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        
        return preferences.getBoolean(key, defaultValue);
    }
    
    public int getPreference(String key, int defaultValue)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        
        if (key.equals("timerAmount"))
        {
            if (preferences.getInt(key, defaultValue) == 0)
            {
                return 1;
            }
        }
        
        return preferences.getInt(key, defaultValue);
    }
    
    public String getPreference(String key, String defaultValue)
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        
        return preferences.getString(key, defaultValue);
    }
}
