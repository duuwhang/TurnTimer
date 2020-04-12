package com.turntimer;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.Toast;
import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    static int screenWidth;
    static int screenHeight;
    static int statusBarSize;
    static int actionBarSize;
    static int navigationBarSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        final Context context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            statusBarSize = getResources().getDimensionPixelSize(resourceId);
        }
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize}
        );
        actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            navigationBarSize = getResources().getDimensionPixelSize(resourceId);
        }

        MainLayout mainLayout = new MainLayout(context);
        setContentView(mainLayout);
    }

    public static int dpToPx(Context context, float dp)
    {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static float pxToDp(Context context, int px)
    {
        return ((float) px / context.getResources().getDisplayMetrics().density);
    }

    public static void debug(Context context)
    {
        Toast.makeText(context, "debug", Toast.LENGTH_LONG).show();
    }

    public static void debug(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
