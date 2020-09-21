package com.turntimer;

import android.content.Context;
import android.widget.Toast;

public class ActivityController
{
    private Context context;
    
    public ActivityController(Context context)
    {
        this.context = context;
    }
    
    public Context GetContext()
    {
        return context;
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
