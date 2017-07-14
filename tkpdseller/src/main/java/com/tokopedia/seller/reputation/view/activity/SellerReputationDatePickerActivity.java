package com.tokopedia.seller.reputation.view.activity;

import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.tokopedia.seller.common.datepicker.view.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.DatePickerTabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 7/11/17.
 */

public class SellerReputationDatePickerActivity extends DatePickerActivity {

    public static final int OFFSCREEN_PAGE_LIMIT = 1;

    @Override
    protected void setupLayout() {
        super.setupLayout();
        tabLayout.setVisibility(View.GONE);
    }

    protected PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(getDatePickerCustomFragment());
        return new DatePickerTabPagerAdapter(getFragmentManager(), fragmentList);
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }
}
