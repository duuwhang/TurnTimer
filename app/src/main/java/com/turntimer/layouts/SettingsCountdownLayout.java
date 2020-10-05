package com.turntimer.layouts;

import android.content.Context;
import android.graphics.Rect;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.turntimer.MainActivity;

public class SettingsCountdownLayout extends BaseLayout
{
    private boolean focusable = true;
    private TextView textView;
    private CheckBox checkBox;
    private EditText editText;
    private EditText editText1;
    private Spinner dropDown;
    private Rect tempChildRect = new Rect();
    
    public SettingsCountdownLayout(Context context)
    {
        super(context);
        
        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText("Countdown Mode ");
        this.addView(textView);
        
        checkBox = new CheckBox(context);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (focusable)
                {
                    SettingsStopwatchLayout stopwatchLayout = ((SpecificSettingsLayout) getParent()).GetStopwatchLayout();
                    stopwatchLayout.ToggleChecked();
                }
            }
        });
        this.addView(checkBox);
        
        editText = new EditText(context);
        editText.setText("" + 5);
        this.addView(editText);
        
        dropDown = new Spinner(context);
        String[] items = new String[]{"min", "sec"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(arrayAdapter);
        this.addView(dropDown);
        
        editText1 = new EditText(context);
        editText1.setText("000");
        this.addView(editText1);
        editText1.setVisibility(INVISIBLE);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        top = 0;
        int currentLeft = left;
        int childrenMaxHeight = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            if (getChildAt(i).getMeasuredHeight() > childrenMaxHeight)
            {
                childrenMaxHeight = getChildAt(i).getMeasuredHeight();
            }
        }
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            if (getChildAt(i) == editText)
            {
                childWidth = editText1.getMeasuredWidth();
            }
            
            tempChildRect.left = currentLeft;
            tempChildRect.top = top + childrenMaxHeight / 2 - childHeight / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            currentLeft = tempChildRect.right;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    protected void ToggleChecked()
    {
        focusable = false;
        checkBox.setChecked(!checkBox.isChecked());
        MainLayout mainLayout = (MainActivity.getInstance()).GetLayout();
        TimerParentLayout timerParentLayout = mainLayout.GetTimerParentLayout();
        if (checkBox.isChecked())
        {
            timerParentLayout.ChangeTimerMode(TimerParentLayout.timerMode.Countdown);
        }
        else
        {
            timerParentLayout.ChangeTimerMode(TimerParentLayout.timerMode.Stopwatch);
        }
        focusable = true;
    }
}