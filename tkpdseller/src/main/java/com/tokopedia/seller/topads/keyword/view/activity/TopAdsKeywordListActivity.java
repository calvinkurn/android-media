package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.DatePickerTabListener;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseKeywordListFragment;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordListActivity extends TActivity implements HasComponent<AppComponent>, SearchView.OnQueryTextListener {
    public static final int OFFSCREEN_PAGE_LIMIT = 2;
    private ViewPager viewPager;
    private TopAdsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        pagerAdapter = getViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        DatePickerTabListener tabListener = new DatePickerTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(tabListener);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.key_word));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.negative));
    }

    private TopAdsPagerAdapter getViewPagerAdapter() {
        String[] titles = {
                getString(R.string.key_word),
                getString(R.string.negative)
        };
        return new TopAdsPagerAdapter(getSupportFragmentManager(), titles);
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_ads_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (getTopAdsBaseKeywordListFragment() != null) {
            getTopAdsBaseKeywordListFragment().onSearchChanged(query);
        }
        return true;
    }

    private TopAdsBaseKeywordListFragment getTopAdsBaseKeywordListFragment() {
        Fragment registeredFragment = pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (registeredFragment != null && registeredFragment.isVisible()) {
            if (registeredFragment instanceof TopAdsBaseKeywordListFragment) {
                return ((TopAdsBaseKeywordListFragment) registeredFragment);
            }
        }
        return null;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (getTopAdsBaseKeywordListFragment() != null) {
            if (TextUtils.isEmpty(newText)) {
                onQueryTextSubmit(newText);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_date) {
            if (getTopAdsBaseKeywordListFragment() != null) {
                getTopAdsBaseKeywordListFragment().onFilterChanged("sekarang");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
