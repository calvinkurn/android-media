package com.tokopedia.topads.keyword.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseTabActivity;
import com.tokopedia.seller.common.datepicker.view.listener.DatePickerTabListener;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAdListFragment;
import com.tokopedia.topads.dashboard.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordListFragment;
import com.tokopedia.topads.keyword.view.listener.AdListMenuListener;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordListActivity extends BaseTabActivity implements
        HasComponent<AppComponent>,
        TopAdsAdListFragment.OnAdListFragmentListener, TopAdsKeywordListFragment.GroupTopAdsListener {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;
    private static final String TAG = TopAdsKeywordListActivity.class.getName();
    private static final int DELAY_SHOW_CASE_THREAD = 300;//ms
    boolean isShowingShowCase = false;
    private ShowCaseDialog showCaseDialog;
    private SearchView searchView;
    private MenuItem searchItem;
    private int totalGroupAd;
    private MenuItem filter;

    @Override
    protected boolean isAllowElevation() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalGroupAd = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, 0);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.addOnTabSelectedListener(new DatePickerTabListener(viewPager));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title_negative));
    }

    @Override
    protected TopAdsPagerAdapter getViewPagerAdapter() {
        String[] titles = {
                getString(R.string.top_ads_keyword_title),
                getString(R.string.top_ads_keyword_title_negative)
        };
        return new TopAdsPagerAdapter(getSupportFragmentManager(), titles);
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
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
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_top_ads_list, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            if (getTopAdsBaseKeywordListFragment() != null) {
                getTopAdsBaseKeywordListFragment().onCreateAd();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startShowCase() {
        if (ShowCasePreference.hasShown(this, TAG)) {
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
                displayShowCase();
            }
        });
    }

    private void displayShowCase() {
        final TopAdsKeywordListFragment topAdsKeywordListFragment = (TopAdsKeywordListFragment) getCurrentFragment();
        if (topAdsKeywordListFragment == null || topAdsKeywordListFragment.getView() == null) {
            return;
        }
        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        View searchView = topAdsKeywordListFragment.getSearchView();
        if (searchView == null) {
            return;
        }
        // Pencarian
        showCaseList.add(
                new ShowCaseObject(
                        searchView,
                        getString(R.string.topads_showcase_keyword_list_title_1),
                        getString(R.string.topads_showcase_keyword_list_desc_1),
                        ShowCaseContentPosition.UNDEFINED,
                        Color.WHITE));

        // Filter
        showCaseList.add(
                new ShowCaseObject(
                        topAdsKeywordListFragment.getFilterView(),
                        getString(R.string.topads_showcase_keyword_list_title_2),
                        getString(R.string.topads_showcase_keyword_list_desc_2),
                        ShowCaseContentPosition.UNDEFINED));

        RecyclerView recyclerView = topAdsKeywordListFragment.getRecyclerView();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (topAdsKeywordListFragment.getView() == null) {
                    return;
                }
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
                showCaseDialog.show(TopAdsKeywordListActivity.this, TAG, showCaseList);
            }
        }, DELAY_SHOW_CASE_THREAD);
    }

    @Override
    public int getGroupTopAdsSize() {
        return totalGroupAd;
    }

    @Override
    public void setGroupTopAdsSize(int size) {
        totalGroupAd = size;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}