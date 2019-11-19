package com.tokopedia.seller.reputation.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;

import com.tokopedia.datepicker.range.view.activity.DatePickerActivity;
import com.tokopedia.datepicker.range.view.adapter.DatePickerTabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 7/11/17.
 */

public class SellerReputationDatePickerActivity extends DatePickerActivity {

    public static final int OFFSCREEN_PAGE_LIMIT = 1;

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.setVisibility(View.GONE);
    }

    protected PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(getDatePickerCustomFragment());
        return new DatePickerTabPagerAdapter(this, getSupportFragmentManager(), fragmentList);
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }
}
