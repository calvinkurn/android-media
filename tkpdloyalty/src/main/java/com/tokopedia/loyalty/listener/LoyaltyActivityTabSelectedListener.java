package com.tokopedia.loyalty.listener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;

/**
 * Created by kris on 12/13/17. Tokopedia
 */

public class LoyaltyActivityTabSelectedListener extends GlobalMainTabSelectedListener {
    public LoyaltyActivityTabSelectedListener(ViewPager mViewPager) {
        super(mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if(tab.getPosition() == 1) {
            UnifyTracking.eventMyCouponClicked();
        }
    }
}
