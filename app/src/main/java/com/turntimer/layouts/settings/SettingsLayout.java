package com.turntimer.layouts.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import com.turntimer.MainActivity;
import com.turntimer.R;
import com.turntimer.layouts.BaseLayout;
import com.turntimer.layouts.timers.TimersParentLayout;
import static com.turntimer.MainActivity.displayMetricsController;

public class SettingsLayout extends BaseLayout
{
    private SettingLayout timerAmountSettingLayout;
    private SettingLayout countdownSettingLayout;
    private SettingLayout stopwatchSettingLayout;
    private SettingLayout saveSettingLayout;
    private int timerAmount;
    private int selection;
    private float timerTime;
    private boolean saveState;
    private String timeUnit;
    private TimersParentLayout.TimerMode timerMode;
    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private final Rect childRect = new Rect();
    
    public SettingsLayout(Context context)
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
        int height = bottom - top;
        
        int childrenMaxWidth = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            if (getChildAt(i).getMeasuredWidth() > childrenMaxWidth)
            {
                childrenMaxWidth = getChildAt(i).getMeasuredWidth();
            }
        }
        
        int margin = Integer.min(displayMetricsController.dpToPx(75), (int) (height * 1.1f / getChildCount()));
        childRect.setEmpty();
        
        for (int i = 0; i < getChildCount(); i++)
        {
            childRect.left = width / 2 - childrenMaxWidth / 2;
            childRect.top = Integer.max(childRect.bottom,
                i * margin
                + ((SettingLayout) getChildAt(0)).getChildAt(0).getMeasuredHeight() / 2
                - ((SettingLayout) getChildAt(i)).getChildAt(0).getMeasuredHeight() / 2);
            childRect.right = childRect.left + getChildAt(i).getMeasuredWidth();
            childRect.bottom = childRect.top + getChildAt(i).getMeasuredHeight();
            getChildAt(i).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
    
    private void constructTimerAmountSetting()
    {
        EditText editText = new EditText(context);
        
        timerAmountSettingLayout = new SettingLayout(context, context.getString(R.string.timer_amount) + " (1-30): ", editText);
        addView(timerAmountSettingLayout);
    }
    
    @SuppressLint("SetTextI18n")
    private void initTimerAmountSetting()
    {
        EditText editText = (EditText) timerAmountSettingLayout.getChildAt(1);
        editText.setText(Integer.toString(timerAmount));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        editText.setWidth(displayMetricsController.dpToPx(32));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                    timerAmount = Math.max(1, Math.min(30, Integer.parseInt(editable.toString())));
                }
                catch (NumberFormatException exception)
                {
                    timerAmount = 1;
                }
                
                TimersParentLayout timersParentLayout = MainActivity.getInstance().getLayout().getTimersParentLayout();
                timersParentLayout.setTimerAmount(timerAmount);
                timersParentLayout.resetTimers();
            }
        });
    }
    
    private void constructCountdownSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        EditText editText = new EditText(context);
        Spinner dropDown = new Spinner(context);
        
        countdownSettingLayout = new SettingLayout(context, context.getString(R.string.countdown_mode) + " ",
            checkBox,
            editText,
            dropDown);
        addView(countdownSettingLayout);
    }
    
    @SuppressLint("SetTextI18n")
    private void initCountdownSetting()
    {
        CheckBox checkBox = (CheckBox) countdownSettingLayout.getChildAt(1);
        checkBoxes.add(checkBox);
        checkBox.setChecked(timerMode != TimersParentLayout.TimerMode.Stopwatch);
        checkBox.setOnClickListener(view ->
        {
            view.setEnabled(false);
            toggleMode();
            view.setEnabled(true);
        });
        checkBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            if (checked)
            {
                changeTimerMode(TimersParentLayout.TimerMode.Countdown);
            }
        });
        
        EditText editText = (EditText) countdownSettingLayout.getChildAt(2);
        editText.setText(Float.toString(timerTime));
        editText.setWidth(displayMetricsController.dpToPx(50));
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
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
                Spinner dropDown = (Spinner) countdownSettingLayout.getChildAt(3);
                changeTimerTimes(editable, dropDown);
            }
        });
        
        final Spinner dropDown = (Spinner) countdownSettingLayout.getChildAt(3);
        String[] items = new String[]{context.getString(R.string.minutes_short), context.getString(R.string.seconds_short)};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(arrayAdapter);
        selection = 0;
        dropDown.setSelection(selection);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (dropDown.getSelectedItemId() != selection)
                {
                    EditText editText = (EditText) countdownSettingLayout.getChildAt(2);
                    Spinner dropDown = (Spinner) countdownSettingLayout.getChildAt(3);
                    changeTimerTimes(editText.getText(), dropDown);
                    selection = (int) dropDown.getSelectedItemId();
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        if (timeUnit.equals(context.getString(R.string.seconds_short)))
        {
            selection = 1;
            dropDown.setSelection(selection);
        }
    }
    
    private void constructStopwatchSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        
        stopwatchSettingLayout = new SettingLayout(context, context.getString(R.string.stopwatch_mode) + " ", checkBox);
        addView(stopwatchSettingLayout);
    }
    
    private void initStopwatchSetting()
    {
        CheckBox checkBox = (CheckBox) stopwatchSettingLayout.getChildAt(1);
        checkBoxes.add(checkBox);
        checkBox.setChecked(timerMode == TimersParentLayout.TimerMode.Stopwatch);
        checkBox.setOnClickListener(view ->
        {
            view.setEnabled(false);
            toggleMode();
            view.setEnabled(true);
        });
        checkBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            if (checked)
            {
                changeTimerMode(TimersParentLayout.TimerMode.Stopwatch);
            }
        });
    }
    
    private void constructSaveSetting()
    {
        CheckBox checkBox = new CheckBox(context);
        
        saveSettingLayout = new SettingLayout(context, context.getString(R.string.save_state) + " ", checkBox);
        addView(saveSettingLayout);
    }
    
    private void initSaveSetting()
    {
        CheckBox checkBox = (CheckBox) saveSettingLayout.getChildAt(1);
        checkBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            MainActivity.getInstance().setSaveStateOption(checked);
            saveState = checked;
        });
        checkBox.setChecked(saveState);
    }
    
    private void changeTimerTimes(Editable editable, Spinner spinner)
    {
        float time;
        try
        {
            time = Float.parseFloat(editable.toString());
        }
        catch (NumberFormatException e)
        {
            time = 1;
        }
        timerTime = time;
        timeUnit = spinner.getSelectedItem().toString();
        
        TimersParentLayout timersParentLayout = MainActivity.getInstance().getLayout().getTimersParentLayout();
        timersParentLayout.setTimeUnit(timeUnit);
        timersParentLayout.setCountdownTime(timerTime);
        timersParentLayout.resetTimers();
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
    
    private void changeTimerMode(TimersParentLayout.TimerMode mode)
    {
        TimersParentLayout timersParentLayout = MainActivity.getInstance().getLayout().getTimersParentLayout();
        timersParentLayout.setTimerMode(mode);
        timersParentLayout.resetTimers();
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
    
    public void setTimerMode(TimersParentLayout.TimerMode timerMode)
    {
        this.timerMode = timerMode;
    }
}
