package com.tokopedia.events.view.behaviours;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pranaymohapatra on 16/11/17.
 */

public class ToolBarBehaviour extends CoordinatorLayout.Behavior<Toolbar> {
    private ColorDrawable mStatusBarColorDrawable;
    private int mStatusBarColor;
    private TextView mTitleView;
    private boolean searchedForTitleView = false;

    public ToolBarBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStatusBarColor =  ContextCompat.getColor(context, android.R.color.holo_orange_dark);
        mStatusBarColor = getColorWithAlpha(0, mStatusBarColor);
        mStatusBarColorDrawable = new ColorDrawable(mStatusBarColor);
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public ToolBarBehaviour() {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Toolbar child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, Toolbar child, MotionEvent ev) {
        return ev == null || super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Toolbar child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            float ratio = (float) getCurrentScrollValue(child, dependency) / getTotalScrollRange(child, dependency);
            float alpha = 1f - Math.min(1f, Math.max(0f, ratio));
            int drawableAlpha = (int) (alpha * 255);

            if(drawableAlpha>=253) {
                int color = Color.parseColor("#ff000000");

                child.setTitleTextColor(color);

            } else if (drawableAlpha == 0){
                int color = Color.parseColor("#00000000");
                color = Color.argb(drawableAlpha, Color.red(color), Color.green(color), Color.blue(color));

                child.setTitleTextColor(color);

            }

                child.getBackground().setAlpha(drawableAlpha);




            setStatusBarColor(parent, drawableAlpha);
        }
        return false;
    }

    private void setStatusBarColor(CoordinatorLayout parent, int alpha) {
        ColorDrawable statusBarBackground = (ColorDrawable) parent.getStatusBarBackground();
        statusBarBackground.setColor(getColorWithAlpha(alpha, statusBarBackground.getColor()));
        parent.setStatusBarBackground(statusBarBackground);
    }

    private int getCurrentScrollValue(Toolbar child, View dependency) {
        return dependency.getBottom() - child.getTop();
    }

    private float getTotalScrollRange(Toolbar child, View dependency) {
        return Math.max(dependency.getHeight(), ((AppBarLayout) dependency).getTotalScrollRange()) - child.getTop();
    }

}
