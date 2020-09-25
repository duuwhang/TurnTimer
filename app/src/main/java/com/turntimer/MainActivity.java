package com.turntimer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity
{
    private Context context;
    private Resources resources;
    private WindowManager windowManager;
    private MainLayout mainLayout;
    static ActivityController activityController;
    static DisplayMetricsController displayMetricsController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        context = this;
        resources = getResources();
        windowManager = getWindowManager();
        activityController = new ActivityController(context);
        displayMetricsController = new DisplayMetricsController(context, resources, windowManager);
        
        mainLayout = new MainLayout(context);
        setContentView(mainLayout);
    }
    
    public BaseLayout getLayout()
    {
        return mainLayout;
    }
}
