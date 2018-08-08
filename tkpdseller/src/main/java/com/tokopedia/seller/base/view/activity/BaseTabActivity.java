package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 7/11/17.
 */

public abstract class BaseTabActivity extends BaseToolbarActivity {

    protected ViewPager viewPager;
    protected TabLayout tabLayout;
    private PagerAdapter pagerAdapter;

    protected abstract PagerAdapter getViewPagerAdapter();

    protected abstract int getPageLimit();

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager.setOffscreenPageLimit(getPageLimit());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        pagerAdapter = getViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected boolean isAllowElevation() {
        return false;
    }

    @Override
    protected int getLayoutRes() {
        return isToolbarWhite() ? R.layout.activity_base_tab_white : R.layout.activity_base_tab_seller;
    }

    protected Fragment getCurrentFragment() {
        if (pagerAdapter == null) {
            return null;
        }
        return (Fragment) pagerAdapter.instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
    }
}