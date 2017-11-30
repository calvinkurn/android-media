package com.tokopedia.discovery.newdiscovery.base;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.helper.OfficialStoreQueryHelper;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import java.util.List;

/**
 * Created by hangnadi on 10/3/17.
 */

public class DiscoveryActivity extends BaseDiscoveryActivity implements
        DiscoverySearchView.SearchViewListener,
        DiscoverySearchView.OnQueryTextListener,
        BottomNavigationListener {

    private Toolbar toolbar;
    private FrameLayout container;
    private AHBottomNavigation bottomNavigation;
    protected DiscoverySearchView searchView;
    protected View loadingView;

    private MenuItem searchItem;
    private boolean isLastRequestForceSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        prepareView();
    }

    protected int getLayoutRes() {
        return R.layout.activity_base_discovery;
    }

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.container);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        searchView = (DiscoverySearchView) findViewById(R.id.search);
        loadingView = findViewById(R.id.progressBar);
    }

    protected void prepareView() {
        initToolbar();
        initSearchView();
        showLoadingView(false);
    }

    protected void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public AHBottomNavigation getBottomNavigation() {
        return bottomNavigation;
    }

    private void onSearchingStart(String keyword) {
        searchView.closeSearch();
        showLoadingView(true);
        showContainer(false);
        setToolbarTitle(keyword);
        setLastQuerySearchView(keyword);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.showSearch(false, true);
            }
        });
    }

    protected void setToolbarTitle(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(query);
        }
    }

    private void initSearchView() {
        searchView.setActivity(this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchViewListener(this);
    }

    protected void setLastQuerySearchView(String lastQuerySearchView) {
        searchView.setLastQuery(lastQuerySearchView);
    }

    @Override
    public void onSearchViewShown() {
        bottomNavigation.hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        CommonUtils.forceShowKeyboard(this);
    }

    @Override
    public void onSearchViewClosed() {
        bottomNavigation.restoreBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (OfficialStoreQueryHelper.isOfficialStoreSearchQuery(query)) {
            onHandleOfficialStorePage();
            sendSearchProductGTM(query);
            return true;
        } else {
            switch (searchView.getSuggestionFragment().getCurrentTab()) {
                case SearchMainFragment.PAGER_POSITION_PRODUCT:
                    onProductQuerySubmit(query);
                    sendSearchProductGTM(query);
                    return false;
                case SearchMainFragment.PAGER_POSITION_SHOP:
                    onShopQuerySubmit(query);
                    sendSearchShopGTM(query);
                    return false;
                default:
                    throw new RuntimeException("Please handle this function if you have new tab of suggestion search view.");
            }
        }
    }

    protected void onProductQuerySubmit(String query) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query);
    }

    private void onShopQuerySubmit(String query) {
        setForceSwipeToShop(true);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query);
    }

    private void sendSearchProductGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearch(keyword);
        }
    }

    private void sendSearchShopGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearchShop(keyword);
        }
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoveryVoiceSearch(keyword);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            if (searchView.isFinishOnClose()) {
                finish();
            } else {
                searchView.closeSearch();
            }
        } else {
            finish();
        }
    }

    public void onSuggestionProductClick(String keyword, String categoryID) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                sessionHandler.isV4Login() ?
                        AuthUtil.md5(sessionHandler.getLoginID()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                sessionHandler.isV4Login() ?
                        sessionHandler.getLoginID() :
                        null
        );
        parameter.setDepartmentId(categoryID);
        onSearchingStart(keyword);
        setForceSearch(false);
        getPresenter().requestProduct(parameter, isForceSearch(), isRequestOfficialStoreBanner());
    }

    public void onSuggestionProductClick(String keyword) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(keyword);
    }

    protected void performRequestProduct(String keyword) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                sessionHandler.isV4Login() ?
                        AuthUtil.md5(sessionHandler.getLoginID()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                sessionHandler.isV4Login() ?
                        sessionHandler.getLoginID() :
                        null
        );
        onSearchingStart(keyword);
        getPresenter().requestProduct(parameter, isForceSearch(), isRequestOfficialStoreBanner());
    }

    public void deleteAllRecentSearch() {
        searchView.getSuggestionFragment().deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword) {
        searchView.getSuggestionFragment().deleteRecentSearch(keyword);
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
    }

    public void setSearchQuery(String keyword) {
        searchView.setQuery(keyword, false, true);
    }

    public void adjustViewContainer(int y) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) container.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, y);
        container.setLayoutParams(layoutParams);
    }

    @Override
    public void setupBottomNavigation(List<AHBottomNavigationItem> items,
                                      AHBottomNavigation.OnTabSelectedListener tabSelectedListener) {

        bottomNavigation.setBackgroundResource(R.drawable.bottomtab_background);
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.tkpd_dark_green));
        bottomNavigation.setOnTabSelectedListener(tabSelectedListener);
        bottomNavigation.setUseElevation(true, getResources().getDimension(R.dimen.bottom_navigation_elevation));
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                adjustViewContainer(y);
            }
        });
    }

    @Override
    public void showBottomNavigation() {
        bottomNavigation.restoreBottomNavigation(true);
    }

    @Override
    public void hideBottomNavigation() {
        if (bottomNavigation != null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    @Override
    public void refreshBottomNavigationIcon(List<AHBottomNavigationItem> items) {
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
    }

    @Override
    public void onHandleResponseError() {
        showLoadingView(false);
        showContainer(true);
        NetworkErrorHelper.showEmptyState(this, container, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                performRequestProduct(searchView.getLastQuery());
            }
        });
        hideBottomNavigation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DiscoverySearchView.REQUEST_VOICE:
                    List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && results.size() > 0) {
                        searchView.setQuery(results.get(0), false);
                        sendVoiceSearchGTM(results.get(0));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
