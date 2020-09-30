package com.turntimer;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import static com.turntimer.MainActivity.activityController;
import static com.turntimer.MainActivity.displayMetricsController;

public class SettingsAmountLayout extends BaseLayout
{
    private TextView textView;
    private EditText editText;
    private Rect tempChildRect = new Rect();
    
    public SettingsAmountLayout(Context context)
    {
        super(context);
        
        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText("Timer Amount (1-30): ");
        this.addView(textView);
        
        editText = new EditText(context);
        editText.setText("" + 4);
        
        editText.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if (!b)
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
                    
                    MainActivity mainActivity = (MainActivity) activityController.GetActivity();
                    TimerParentLayout timerParentLayout = (TimerParentLayout) mainActivity.getLayout().getChildAt(1);
                    timerParentLayout.UpdateTimerAmount(timerAmount);
                }
            }
        });
        this.addView(editText);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int height = bottom - top;
        int width = right - left;
        
        for (int i = 0; i < getChildCount(); i++)
        {
            int childHeight = getChildAt(i).getMeasuredHeight();
            int childWidth = getChildAt(i).getMeasuredWidth();
            
            tempChildRect.left = left + width / 2 - childWidth / 2 + i * width / 4;
            tempChildRect.top = top + height / 2 - childHeight / 2;
            tempChildRect.right = tempChildRect.left + childWidth;
            tempChildRect.bottom = tempChildRect.top + childHeight;
            
            getChildAt(i).layout(tempChildRect.left, tempChildRect.top, tempChildRect.right, tempChildRect.bottom);
        }
    }
}