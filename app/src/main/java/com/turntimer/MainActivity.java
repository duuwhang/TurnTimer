package com.turntimer;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import com.turntimer.layouts.MainLayout;

public class MainActivity extends AppCompatActivity
{
    private MainLayout mainLayout;
    private static MainActivity m;
    public static DisplayMetricsController displayMetricsController;
    
    public MainActivity()
    {
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
    
    public MainLayout getLayout()
    {
        return mainLayout;
    }
}
