package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import com.turntimer.MainActivity;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.MainLayout;
import com.turntimer.layouts.timers.TimerParentLayout;

public class SettingsSubLayout extends BaseLayout
{
    private Setting timerAmountSetting;
    private Setting countdownSetting;
    private Setting stopwatchSetting;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private Rect tempChildRect = new Rect();
    
    public SettingsSubLayout(Context context)
    {
        super(context);
        
        addTimerAmountSetting();
        
        addCountdownSetting();
        
        addStopwatchSetting();
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            tempChildRect.left = left;
            tempChildRect.top = top + i * height / getChildCount() - height / getChildCount();
            tempChildRect.right = tempChildRect.left + width;
            tempChildRect.bottom = tempChildRect.top + height / getChildCount();
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
    
    private void addTimerAmountSetting()
    {
        final EditText editText = new EditText(context);
        editText.setText("" + 4);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            
            }
            
            @Override
            public void afterTextChanged(Editable editable)
            {
                int timerAmount;
                try
                {
                    timerAmount = Integer.parseInt(editText.getText().toString());
                }
                catch (NumberFormatException e)
                {
                    timerAmount = 1;
                }
                
                MainLayout mainLayout = (MainActivity.getInstance()).getLayout();
                TimerParentLayout timerParentLayout = mainLayout.getTimerParentLayout();
                timerParentLayout.updateTimerAmount(timerAmount);
            }
        });
        
        timerAmountSetting = new Setting(context, "Timer Amount (1-30): ", editText);
        this.addView(timerAmountSetting);
    }
    
    private void addCountdownSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        checkBoxes.add(checkBox);
        
        checkBox.setChecked(true);
        checkBox.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.setEnabled(false);
                toggleMode();
                view.setEnabled(true);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    changeTimerMode(TimerParentLayout.timerMode.Countdown);
                }
            }
        });
        
        EditText editTime = new EditText(context);
        editTime.setText("" + 5.0f);
        
        Spinner dropDown = new Spinner(context);
        String[] items = new String[]{"min", "sec"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(arrayAdapter);
        
        countdownSetting = new Setting(context, "Countdown Mode ",
            checkBox,
            editTime,
            dropDown);
        this.addView(countdownSetting);
    }
    
    private void addStopwatchSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        checkBoxes.add(checkBox);
        
        checkBox.setChecked(false);
        checkBox.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.setEnabled(false);
                toggleMode();
                view.setEnabled(true);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    changeTimerMode(TimerParentLayout.timerMode.Stopwatch);
                }
            }
        });
        
        stopwatchSetting = new Setting(context, "Stopwatch Mode ", checkBoxes.get(checkBoxes.size() - 1));
        this.addView(stopwatchSetting);
    }
    
    private void toggleMode()
    {
        for (CheckBox checkbox : checkBoxes)
        {
            if (checkbox.isEnabled())
            {
                checkbox.setChecked(!checkbox.isChecked());
            }
        }
    }
    
    private void changeTimerMode(TimerParentLayout.timerMode mode)
    {
        MainLayout mainLayout = (MainActivity.getInstance()).getLayout();
        TimerParentLayout timerParentLayout = mainLayout.getTimerParentLayout();
        timerParentLayout.changeTimerMode(mode);
    }
}