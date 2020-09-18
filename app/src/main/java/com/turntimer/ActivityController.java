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
    
    public Context getContext()
    {
        return context;
    }
    
    public void debug()
    {
        Toast.makeText(context, "debug", Toast.LENGTH_LONG).show();
    }
    
    public void debug(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
