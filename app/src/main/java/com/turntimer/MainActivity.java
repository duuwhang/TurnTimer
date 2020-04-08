package com.turntimer;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int actionBarSize;
    int screenHeight;
    int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        final Context context = this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        actionBarSize = dpToPx(this, context.getResources().getInteger(R.integer.actionBarSize));

        MainLayout mainLayout = new MainLayout(context);
        setContentView(mainLayout);
    }

    public int dpToPx(@NotNull Context context, int dp){
        return (int) ((float) dp * context.getResources().getDisplayMetrics().density);
    }

    public int pxToDp(@NotNull Context context, int px){
        return (int) ((float) px / context.getResources().getDisplayMetrics().density);
    }

    public void debug(@NotNull Context context){
        Toast.makeText(context,"debug",Toast.LENGTH_LONG).show();
    }

    public void debug(@NotNull Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
