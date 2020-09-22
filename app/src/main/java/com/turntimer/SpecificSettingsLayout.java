package com.turntimer;

import android.content.Context;
import android.graphics.Rect;

public class SpecificSettingsLayout extends BaseLayout
{
    Context context;
    private Rect tempChildRect = new Rect();
    
    public SpecificSettingsLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
    
    }
}