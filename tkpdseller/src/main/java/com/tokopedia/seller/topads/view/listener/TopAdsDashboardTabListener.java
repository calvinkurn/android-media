package com.tokopedia.seller.topads.view.listener;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.listener.GlobalMainTabSelectedListener;

/**
 * Created by normansyahputa on 1/24/17.
 */

public class TopAdsDashboardTabListener extends GlobalMainTabSelectedListener {
    public TopAdsDashboardTabListener(ViewPager mViewPager) {
        super(mViewPager);
    }

    public TopAdsDashboardTabListener(Activity activity, ViewPager mViewPager) {
        super(activity, mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if(topAdsDashboardList!=null){
            topAdsDashboardList.onSelected(tab.getPosition());
        }
    }

    TopAdsDashboardList topAdsDashboardList;

    public void setTopAdsDashboardList(TopAdsDashboardList topAdsDashboardList) {
        this.topAdsDashboardList = topAdsDashboardList;
    }

    public interface TopAdsDashboardList{
        void onSelected(int positon);
    }
}
