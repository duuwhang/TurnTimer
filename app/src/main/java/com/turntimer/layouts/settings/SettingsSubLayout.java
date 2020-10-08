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
import com.turntimer.layouts.timers.TimerParentLayout;

public class SettingsSubLayout extends BaseLayout
{
    private Setting timerAmountSetting;
    private Setting countdownSetting;
    private Setting stopwatchSetting;
    private Setting saveSetting;
    private int timerAmount;
    private float timerTime;
    private boolean saveState;
    private String timeUnit;
    private TimerParentLayout.TimerMode timerMode;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private Rect tempChildRect = new Rect();
    
    public SettingsSubLayout(Context context)
    {
        super(context);
        
        constructTimerAmountSetting();
        
        constructCountdownSetting();
        
        constructStopwatchSetting();
        
        constructSaveSetting();
    }
    
    @Override
    public void init()
    {
        initTimerAmountSetting();
        
        initCountdownSetting();
        
        initStopwatchSetting();
        
        initSaveSetting();
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
    
    private void constructTimerAmountSetting()
    {
        EditText editText = new EditText(context);
        
        timerAmountSetting = new Setting(context, "Timer Amount (1-30): ", editText);
        this.addView(timerAmountSetting);
    }
    
    private void initTimerAmountSetting()
    {
        EditText editText = (EditText) timerAmountSetting.getElement(1);
        editText.setText("" + timerAmount);
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
                    EditText editText = (EditText) timerAmountSetting.getElement(1);
                    timerAmount = Math.max(1, Math.min(30, Integer.parseInt(editText.getText().toString())));
                }
                catch (NumberFormatException e)
                {
                    timerAmount = 1;
                }
                
                TimerParentLayout timerParentLayout = MainActivity.getInstance().getLayout().getTimerParentLayout();
                timerParentLayout.setTimerAmount(timerAmount);
                timerParentLayout.updateTimerAmount();
            }
        });
    }
    
    private void constructCountdownSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        EditText editText = new EditText(context);
        Spinner dropDown = new Spinner(context);
        
        countdownSetting = new Setting(context, "Countdown Mode ",
            checkBox,
            editText,
            dropDown);
        this.addView(countdownSetting);
    }
    
    private void initCountdownSetting()
    {
        CheckBox checkBox = (CheckBox) countdownSetting.getElement(1);
        checkBoxes.add(checkBox);
        if (timerMode == TimerParentLayout.TimerMode.Stopwatch)
        {
            checkBox.setChecked(false);
        }
        else
        {
            checkBox.setChecked(true);
        }
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
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                if (checked)
                {
                    changeTimerMode(TimerParentLayout.TimerMode.Countdown);
                }
            }
        });
        
        EditText editText = (EditText) countdownSetting.getElement(2);
        editText.setText("" + timerTime);
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
                EditText editText = (EditText) countdownSetting.getElement(2);
                Spinner dropDown = (Spinner) countdownSetting.getElement(3);
                changeTimerTimes(editText, dropDown);
            }
        });
        
        Spinner dropDown = (Spinner) countdownSetting.getElement(3);
        String[] items = new String[]{"min", "sec"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(arrayAdapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                EditText editText = (EditText) countdownSetting.getElement(2);
                Spinner dropDown = (Spinner) countdownSetting.getElement(3);
                changeTimerTimes(editText, dropDown);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            
            }
        });
        if (timeUnit.equals("sec"))
        {
            dropDown.setSelection(1);
        }
        else
        {
            dropDown.setSelection(0);
        }
    }
    
    private void constructStopwatchSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        
        stopwatchSetting = new Setting(context, "Stopwatch Mode ", checkBox);
        this.addView(stopwatchSetting);
    }
    
    private void initStopwatchSetting()
    {
        CheckBox checkBox = (CheckBox) stopwatchSetting.getElement(1);
        checkBoxes.add(checkBox);
        if (timerMode == TimerParentLayout.TimerMode.Stopwatch)
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
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
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                if (checked)
                {
                    changeTimerMode(TimerParentLayout.TimerMode.Stopwatch);
                }
            }
        });
    }
    
    private void constructSaveSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        
        saveSetting = new Setting(context, "Save State ", checkBox);
        this.addView(saveSetting);
    }
    
    private void initSaveSetting()
    {
        CheckBox checkBox = (CheckBox) saveSetting.getElement(1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                MainActivity.getInstance().setSaveStateOption(checked);
                saveState = checked;
            }
        });
        checkBox.setChecked(saveState);
    }
    
    private void changeTimerTimes(EditText editText, Spinner spinner)
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
        timerTime = time;
        timeUnit = spinner.getSelectedItem().toString();
        
        TimerParentLayout timerParentLayout = MainActivity.getInstance().getLayout().getTimerParentLayout();
        timerParentLayout.setCountdownTime(timerTime);
        timerParentLayout.setTimeUnit(timeUnit);
        timerParentLayout.updateCountdownTime();
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
    
    private void changeTimerMode(TimerParentLayout.TimerMode mode)
    {
        TimerParentLayout timerParentLayout = MainActivity.getInstance().getLayout().getTimerParentLayout();
        timerParentLayout.setTimerMode(mode);
        timerParentLayout.updateTimerMode();
    }
    
    public void setTimerAmount(int timerAmount)
    {
        this.timerAmount = timerAmount;
    }
    
    public void setCountdownTime(float timerTime)
    {
        this.timerTime = timerTime;
    }
    
    public void setSaveState(boolean saveState)
    {
        this.saveState = saveState;
    }
    
    public void setTimeUnit(String timeUnit)
    {
        this.timeUnit = timeUnit;
    }
    
    public void setTimerMode(TimerParentLayout.TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
}