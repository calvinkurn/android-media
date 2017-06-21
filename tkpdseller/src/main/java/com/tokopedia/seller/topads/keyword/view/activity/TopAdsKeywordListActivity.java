package com.tokopedia.seller.topads.keyword.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
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
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordListActivity extends BaseActivity implements
        HasComponent<AppComponent>, SearchView.OnQueryTextListener,
        KeywordListListener.Listener,
        TopAdsAdListFragment.OnAdListFragmentListener,
        TopAdsKeywordListFragment.GroupTopAdsListener {
    public static final int OFFSCREEN_PAGE_LIMIT = 2;
    boolean isShowingShowCase = false;
    private ShowCaseDialog showCaseDialog;
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

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsKeywordListActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)){
            return;
        }
        if (showCaseDialog != null || isShowingShowCase) {
            return;
        }
        isShowingShowCase = true;

        viewPager.setCurrentItem(0);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                final TopAdsKeywordListFragment topAdsKeywordListFragment = getCurrentFragment();
                if (topAdsKeywordListFragment == null) {
                    return;
                }
                final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (toolbar.getHeight() > 0) {
                    final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
                    int height = toolbar.getHeight();
                    int width = toolbar.getWidth();

                    // Pencarian
                    showCaseList.add(
                            new ShowCaseObject(
                                    findViewById(android.R.id.content),
                                    getString(R.string.topads_showcase_keyword_list_title_1),
                                    getString(R.string.topads_showcase_keyword_list_desc_1),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE)
                                    .withCustomTarget(new int[]{width - (int)(height * 1.8), 0,width - (int)(height * 0.8), height}));

                    // Filter
                    showCaseList.add(
                            new ShowCaseObject(
                                    findViewById(android.R.id.content),
                                    getString(R.string.topads_showcase_keyword_list_title_2),
                                    getString(R.string.topads_showcase_keyword_list_desc_2),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE)
                                    .withCustomTarget(new int[]{width - (int)(height * 0.9), 0,width, height}));

                    RecyclerView recyclerView = topAdsKeywordListFragment.getRecyclerView();
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View dateView = topAdsKeywordListFragment.getDateView();
                            if (dateView != null) {
                                dateView.setVisibility(View.VISIBLE);
                                showCaseList.add(
                                        new ShowCaseObject(
                                                dateView,
                                                getString(R.string.topads_showcase_keyword_list_title_3),
                                                getString(R.string.topads_showcase_keyword_list_desc_3)));
                            }

                            View itemView = topAdsKeywordListFragment.getItemRecyclerView();
                            if (itemView != null) {
                                showCaseList.add(
                                        new ShowCaseObject(
                                                itemView,
                                                getString(R.string.topads_showcase_keyword_list_title_4),
                                                getString(R.string.topads_showcase_keyword_list_desc_4),
                                                ShowCaseContentPosition.UNDEFINED,
                                                Color.WHITE));
                            }

                            View fabView = topAdsKeywordListFragment.getFab();
                            if (fabView != null) {
                                showCaseList.add(
                                        new ShowCaseObject(
                                                fabView,
                                                getString(R.string.topads_showcase_keyword_list_title_5),
                                                getString(R.string.topads_showcase_keyword_list_desc_5)));
                            }
                            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                            showCaseDialog.show(TopAdsKeywordListActivity.this, showCaseTag, showCaseList);
                        }
                    }, 300);

                }
                else {
                    isShowingShowCase = false;
                    toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(toolbar,
                            new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    startShowCase();
                                }
                            }));
                }

            }
        });
    }

    @Override
    public int getGroupTopAdsSize() {
        return totalGroupAd;
    }

    @Override
    public void setGroupTopAdsSize(int size) {
        totalGroupAd = size;
    }
}