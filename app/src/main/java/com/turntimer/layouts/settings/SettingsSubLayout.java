package com.turntimer.layouts.settings;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
        int width = right - left;
        int currentTop = 0;
        int heightPadding = getChildAt(0).getMeasuredHeight();
        
        int childrenMaxWidth = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            if (getChildAt(i).getMeasuredWidth() > childrenMaxWidth)
            {
                childrenMaxWidth = getChildAt(i).getMeasuredWidth();
            }
        }
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            
            tempChildRect.left = left + width / 2 - childrenMaxWidth / 2;
            tempChildRect.top = currentTop;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            currentTop = tempChildRect.bottom + heightPadding;
            
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
                    timerAmount = Math.max(1, Math.min(30, Integer.parseInt(editText.getText().toString())));
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
        
        final EditText editText = new EditText(context);
        final Spinner dropDown = new Spinner(context);
        
        editText.setText("" + 5.0);
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
                updateTimerTimes(editText, dropDown);
            }
        });
        
        String[] items = new String[]{"min", "sec"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(arrayAdapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                updateTimerTimes(editText, dropDown);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            
            }
        });
        
        countdownSetting = new Setting(context, "Countdown Mode ",
            checkBox,
            editText,
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
    
    private void updateTimerTimes(EditText editText, Spinner spinner)
    {
        
        float time;
        try
        {
            time = Float.parseFloat(editText.getText().toString());
        }
        catch (NumberFormatException e)
        {
            time = 1;
        }
        
        if (spinner.getSelectedItem().toString().equals("min"))
        {
            time *= 60;
        }
        
        MainLayout mainLayout = (MainActivity.getInstance()).getLayout();
        TimerParentLayout timerParentLayout = mainLayout.getTimerParentLayout();
        timerParentLayout.setTimerCountdownTime((int) time * 1000);
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