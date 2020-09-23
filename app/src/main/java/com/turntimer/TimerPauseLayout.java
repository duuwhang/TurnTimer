package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.TextView;

public class TimerPauseLayout extends BaseLayout
{
    TextView timersPausedText;
    Button resetButton;
    
    public TimerPauseLayout(Context context)
    {
        super(context);
        
        timersPausedText = new TextView(context);
        timersPausedText.setText("Timers Paused");
        timersPausedText.setTextSize(50);
        this.addView(timersPausedText);
        
        resetButton = new Button(context);
        resetButton.setText("Reset Timers");
        this.addView(resetButton);
    }
}