package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.TextView;

public class TimerPauseLayout extends BaseLayout
{
    Context context;
    TextView timersPausedText;
    Button resetButton;
    private Rect tempChildRect = new Rect();
    
    public TimerPauseLayout(Context context)
    {
        super(context);
        this.context = context;
        Init();
    }
    
    private void Init()
    {
        timersPausedText = new TextView(context);
        timersPausedText.setText("Timers Paused");
        timersPausedText.setTextSize(50);
        this.addView(timersPausedText);
        
        resetButton = new Button(context);
        resetButton.setText("Reset Timers");
        //this.addView(resetButton);
    }
}