package com.turntimer;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TimerLayout extends RelativeLayout
{
    Context context;
    Button button;

    public TimerLayout(Context context)
    {
        super(context);
        this.context = context;
        button = new Button(context);
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.addView(button);
    }
}
