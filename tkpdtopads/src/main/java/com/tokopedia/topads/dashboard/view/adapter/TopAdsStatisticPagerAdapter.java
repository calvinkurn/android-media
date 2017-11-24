package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.topads.R;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/5/17.
 */

public class TopAdsStatisticPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;

    public TopAdsStatisticPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
                return MainApplication.getAppContext().getString(R.string.label_top_ads_impression);
            case 1:
                return MainApplication.getAppContext().getString(R.string.label_top_ads_click);
            case 2:
                return MainApplication.getAppContext().getString(R.string.label_top_ads_ctr);
            case 3:
                return MainApplication.getAppContext().getString(R.string.label_top_ads_conversion);
            case 4:
                return MainApplication.getAppContext().getString(R.string.label_top_ads_average);
            case 5:
                return MainApplication.getAppContext().getString(R.string.label_top_ads_cost);
            default:
                return "";
        }
    }

}
