package com.tokopedia.transaction.orders.orderlist.view.listener;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Angga.Prasetiyo on 01/02/2016.
 */
public class GlobalMainTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private static final String TAG = GlobalMainTabSelectedListener.class.getSimpleName();
    private final ViewPager viewPager;
    private Activity activity;

    public GlobalMainTabSelectedListener(ViewPager mViewPager) {
        this.viewPager = mViewPager;
    }

    public GlobalMainTabSelectedListener(Activity activity, ViewPager mViewPager) {
        this.viewPager = mViewPager;
        this.activity = activity;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if(activity!=null) {
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                 hiddenKeyboard(focus,this.activity);
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if(activity!=null) {
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                hiddenKeyboard(focus,this.activity);
            }
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if(activity!=null) {
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                hiddenKeyboard(focus,this.activity);
            }
         }
    }

    private void hiddenKeyboard(View v, Activity activity) {
        InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
