package com.tokopedia.seller.common.datepicker.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.seller.R;

import java.util.List;

/**
 * Created by Nathaniel on 01/23/2017.
 */
public class DatePickerTabPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;


    public DatePickerTabPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
                return MainApplication.getAppContext().getString(R.string.label_date_period);
            case 1:
                return MainApplication.getAppContext().getString(R.string.label_date_custom);
            default:
                return MainApplication.getAppContext().getString(R.string.label_date_period);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
