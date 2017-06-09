package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.DatePickerTabListener;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordListFragment;
import com.tokopedia.seller.topads.keyword.view.listener.AdListMenuListener;
import com.tokopedia.seller.topads.keyword.view.listener.KeywordListListener;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordListActivity extends BaseActivity implements
        HasComponent<AppComponent>, SearchView.OnQueryTextListener,
        KeywordListListener.Listener {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;

    private ViewPager viewPager;
    private TopAdsPagerAdapter pagerAdapter;
    private SearchView searchView;
    private KeywordListListener keywordListTablayout;
    private MenuItem searchItem;

    private int totalGroupAd;
    private MenuItem filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_list);
        totalGroupAd = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.indicator);
        keywordListTablayout = new KeywordListListener(tabLayout, this);
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
        viewPager.addOnPageChangeListener(keywordListTablayout);
        DatePickerTabListener tabListener = new DatePickerTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(tabListener);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title_negative));
    }

    private void fabOnClick() {
        getTopAdsBaseKeywordListFragment().onCreateAd();
    }

    private TopAdsPagerAdapter getViewPagerAdapter() {
        String[] titles = {
                getString(R.string.top_ads_keyword_title),
                getString(R.string.top_ads_keyword_title_negative)
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
        getMenuInflater().inflate(R.menu.menu_keyword_top_ads_list, menu);

        filter = menu.findItem(R.id.menu_filter);
        searchItem = menu.findItem(R.id.menu_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                keywordListTablayout.add(viewPager.getCurrentItem());
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                keywordListTablayout.remove(viewPager.getCurrentItem());
                return true;
            }
        });
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        keywordListTablayout.attachSearchView(searchView);

        filter.setVisible(false);
        searchItem.setVisible(false);
        searchView.setVisibility(View.GONE);

        return super.onCreateOptionsMenu(menu);
    }

    public void validateMenuItem() {
        TopAdsKeywordListFragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment.hasDataFromServer()) {
            filter.setVisible(true);
            searchItem.setVisible(true);
            searchView.setVisibility(View.VISIBLE);
        } else {
            filter.setVisible(false);
            searchItem.setVisible(false);
            searchView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (getTopAdsBaseKeywordListFragment() != null) {
            getTopAdsBaseKeywordListFragment().onSearch(query);
        }
        return true;
    }

    private AdListMenuListener getTopAdsBaseKeywordListFragment() {
        Fragment registeredFragment = getCurrentFragment();
        if (registeredFragment != null && registeredFragment.isVisible()) {
            if (registeredFragment instanceof AdListMenuListener) {
                return ((AdListMenuListener) registeredFragment);
            }
        }
        return null;
    }

    private TopAdsKeywordListFragment getCurrentFragment() {
        Fragment registeredFragment = pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (registeredFragment != null && registeredFragment instanceof TopAdsBaseListFragment) {
            return (TopAdsKeywordListFragment) registeredFragment;
        }
        return null;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (getTopAdsBaseKeywordListFragment() != null) {
            if (TextUtils.isEmpty(newText)) {
                onQueryTextSubmit(null);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_filter) {
            if (getTopAdsBaseKeywordListFragment() != null) {
                getTopAdsBaseKeywordListFragment().goToFilter();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeListener() {
        searchView.setOnQueryTextListener(null);
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        searchItem.collapseActionView();
    }

    @Override
    public void addListener() {
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void expand() {
        searchItem.expandActionView();
    }
}