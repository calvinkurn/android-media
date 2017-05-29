package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseKeywordListFragment;
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
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.indicator);
        keywordListTablayout = new KeywordListListener(tabLayout, this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fab = (FloatingActionButton) findViewById(R.id.top_ads_dashboard_keyword_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabOnClick();
            }
        });

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
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_key_word));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_negative));
    }

    private void fabOnClick() {
        getTopAdsBaseKeywordListFragment().onCreateKeyword();
    }

    private TopAdsPagerAdapter getViewPagerAdapter() {
        String[] titles = {
                getString(R.string.top_ads_key_word),
                getString(R.string.top_ads_negative)
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
        return super.onCreateOptionsMenu(menu);
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
                onQueryTextSubmit(null);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            if (getTopAdsBaseKeywordListFragment() != null) {
                getTopAdsBaseKeywordListFragment().onFilterChanged("sekarang");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeListener() {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(null);
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
