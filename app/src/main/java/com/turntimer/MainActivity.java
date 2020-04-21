package com.turntimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    Context context;
    Resources resources;
    WindowManager windowManager;
    @SuppressLint("StaticFieldLeak")
    static DisplayMetricsController displayMetricsController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        context = this;
        resources = getResources();
        windowManager = getWindowManager();
        displayMetricsController = new DisplayMetricsController(context, resources, windowManager);
        
        MainLayout mainLayout = new MainLayout(context);
        setContentView(mainLayout);
    }
    
    public static void Debug(Context context)
    {
        Toast.makeText(context, "debug", Toast.LENGTH_LONG).show();
    }
    
    public static void Debug(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
