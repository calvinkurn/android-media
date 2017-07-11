package com.tokopedia.seller.base.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 7/11/17.
 */

public abstract class BaseTabActivity extends BaseToolbarActivity {

    protected ViewPager viewPager;
    protected TabLayout tabLayout;

    protected abstract PagerAdapter getViewPagerAdapter();

    protected abstract int getPageLimit();

    @Override
    protected void setupLayout() {
        super.setupLayout();
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.indicator);
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(getPageLimit());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_date_picker;
    }
}
