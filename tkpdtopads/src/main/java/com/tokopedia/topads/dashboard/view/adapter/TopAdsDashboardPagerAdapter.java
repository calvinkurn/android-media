package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.topads.R;

import java.util.List;

/**
 * Created by Nathaniel on 01/23/2017.
 */
public class TopAdsDashboardPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;


    public TopAdsDashboardPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return MainApplication.getAppContext().getString(R.string.top_ads_title_product);
            case 1:
                return MainApplication.getAppContext().getString(R.string.title_top_ads_store);
            default:
                return MainApplication.getAppContext().getString(R.string.top_ads_title_product);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
