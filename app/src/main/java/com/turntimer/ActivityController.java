package com.turntimer;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ActivityController
{
    private Context context;
    private Activity activity;
    
    public ActivityController(Context context)
    {
        this.context = context;
        this.activity = (Activity) context;
    }
    
    public Context GetContext()
    {
        return context;
    }
    
    public Context GetActivity()
    {
        return activity;
    }
    
    public void Debug()
    {
        Toast.makeText(context, "debug", Toast.LENGTH_LONG).show();
    }
    
    public void Debug(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
