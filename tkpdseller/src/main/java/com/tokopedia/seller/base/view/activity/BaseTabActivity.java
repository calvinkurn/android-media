package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 7/11/17.
 */
@Deprecated
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