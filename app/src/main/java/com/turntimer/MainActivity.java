package com.turntimer;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import com.turntimer.layouts.MainLayout;

import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity
{
    private static MainActivity m;
    private MainLayout mainLayout;
    public static DisplayMetricsController displayMetricsController;

    public MainActivity() {
        m = this;
    }

    public static MainActivity getInstance()
    {
        return m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Resources resources = getResources();
        WindowManager windowManager = getWindowManager();
        displayMetricsController = new DisplayMetricsController(windowManager.getDefaultDisplay(), resources.getDisplayMetrics().density);
        
        mainLayout = new MainLayout(this);
        setContentView(mainLayout);
    }
    
    public MainLayout GetLayout()
    {
        return mainLayout;
    }
}
