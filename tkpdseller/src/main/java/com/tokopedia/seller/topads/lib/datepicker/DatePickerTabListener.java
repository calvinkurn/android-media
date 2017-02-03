package com.tokopedia.seller.topads.lib.datepicker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.listener.GlobalMainTabSelectedListener;

/**
 * Created by normansyahputa on 1/24/17.
 */

public class DatePickerTabListener extends GlobalMainTabSelectedListener {

    private TopAdsDashboardList topAdsDashboardList;

    public DatePickerTabListener(ViewPager mViewPager) {
        super(mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if (topAdsDashboardList != null) {
            topAdsDashboardList.onSelected(tab.getPosition());
        }
    }

    public void setTopAdsDashboardList(TopAdsDashboardList topAdsDashboardList) {
        this.topAdsDashboardList = topAdsDashboardList;
    }

    public interface TopAdsDashboardList {
        void onSelected(int positon);
    }
}
