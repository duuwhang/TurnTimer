package com.turntimer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class MainLayout extends ViewGroup {
    Context context;

    public MainLayout(Context context) {
        super(context);
        this.context = context;
    }

    public MainLayout(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
        this.context = context;
    }

    public MainLayout(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
