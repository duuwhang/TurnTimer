package com.turntimer;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.settings.SettingsSubLayout;
import com.turntimer.layouts.timers.TimerLayout;
import com.turntimer.layouts.timers.TimerParentLayout;

public class MainActivity extends AppCompatActivity
{
    private boolean saveState;
    private boolean loading;
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
            TimerParentLayout timerParentLayout = getLayout().getTimerParentLayout();
            
            if (timerParentLayout.getTimerMode() == TimerParentLayout.TimerMode.Stopwatch)
            {
                editor.putInt("timerMode", 1);
            }
            else
            {
                editor.putInt("timerMode", 0);
            }
            editor.putFloat("countdownTime", timerParentLayout.getCountdownTime());
            editor.putString("countdownUnit", timerParentLayout.getTimeUnit());
            editor.putInt("activeTimerId", timerParentLayout.getActiveTimerId());
            int timerAmount = timerParentLayout.getTimerAmount();
            editor.putInt("timerAmount", timerAmount);
            for (int i = 0; i < timerAmount; i++)
            {
                TimerLayout timerLayout = (TimerLayout) timerParentLayout.getChildAt(i);
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
            if (!preferences.getBoolean("showedHint", false))
            {
                Toast.makeText(this, "Swipe right to view the timers", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("showedHint", true);
                editor.apply();
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
            
            TimerParentLayout timerParentLayout = getLayout().getTimerParentLayout();
            SettingsSubLayout settingsSubLayout = getLayout().getSettingsLayout().getSettingsSubLayout();
            
            settingsSubLayout.setSaveState(saveState);
            if (preferences.getInt("timerMode", 0) == 1)
            {
                timerParentLayout.setTimerMode(TimerParentLayout.TimerMode.Stopwatch);
                settingsSubLayout.setTimerMode(TimerParentLayout.TimerMode.Stopwatch);
            }
            else
            {
                timerParentLayout.setTimerMode(TimerParentLayout.TimerMode.Countdown);
                settingsSubLayout.setTimerMode(TimerParentLayout.TimerMode.Countdown);
            }
            
            timerParentLayout.setTimeUnit(preferences.getString("countdownUnit", ""));
            settingsSubLayout.setTimeUnit(preferences.getString("countdownUnit", ""));
            
            timerParentLayout.setCountdownTime(preferences.getFloat("countdownTime", 0.0f));
            settingsSubLayout.setCountdownTime(preferences.getFloat("countdownTime", 0.0f));
            
            timerParentLayout.setTimerAmount(preferences.getInt("timerAmount", 0));
            settingsSubLayout.setTimerAmount(preferences.getInt("timerAmount", 0));
            
            timerParentLayout.setActiveTimerId(preferences.getInt("activeTimerId", 0));
            
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
    
    public MainLayout getLayout()
    {
        return mainLayout;
    }
    
    public void setSaveStateOption(boolean saveState)
    {
        this.saveState = saveState;
    }
    
    public boolean getSaveStateOption()
    {
        return saveState;
    }
    
    public boolean getLoading()
    {
        return loading;
    }
}
