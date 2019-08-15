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
import android.widget.ProgressBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
import com.tokopedia.discovery.newdiscovery.helper.UrlParamHelper;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;
import com.tokopedia.discovery.util.AutoCompleteTracking;
import com.tokopedia.track.TrackApp;

import java.util.List;

import static com.tokopedia.discovery.common.constants.SearchConstant.AUTO_COMPLETE_ACTIVITY_RESULT_CODE_FINISH_ACTIVITY;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_FORCE_SWIPE_TO_SHOP;

/**
 * Created by hangnadi on 10/3/17.
 */
public class DiscoveryActivity extends BaseDiscoveryActivity implements
        DiscoverySearchView.SearchViewListener,
        DiscoverySearchView.ImageSearchClickListener,
        DiscoverySearchView.OnQueryTextListener,
        BottomNavigationListener {

    private Toolbar toolbar;
    private FrameLayout container;
    private AHBottomNavigation bottomNavigation;
    protected DiscoverySearchView searchView;
    protected ProgressBar loadingView;

    public MenuItem searchItem;

    protected TkpdProgressDialog tkpdProgressDialog;
    protected boolean isFromCamera = false;

    protected View root;

    protected SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        proceed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        proceed();
    }

    private void proceed() {
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
        root = findViewById(R.id.root);
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

    protected void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setOnClickListener(v -> searchView.showSearch(false, true));
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
        searchView.setOnImageSearchClickListener(this);
    }

    protected void setLastQuerySearchView(String lastQuerySearchView) {
        searchView.setLastQuery(lastQuerySearchView);
    }

    @Override
    public void onSearchViewShown() {
        hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        CommonUtils.forceShowKeyboard(this);
    }

    @Override
    public void onSearchViewClosed() {
        showBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(SearchParameter searchParameter) {
        this.searchParameter = new SearchParameter(searchParameter);

        String query = searchParameter.getSearchQuery();
        AutoCompleteTracking.eventClickSubmit(this, query);

        clearFocusSearchView();
        handleQueryTextSubmitBasedOnCurrentTab();

        return true;
    }

    private void clearFocusSearchView() {
        if(searchView != null) {
            searchView.clearFocus();
        }
    }

    private void handleQueryTextSubmitBasedOnCurrentTab() throws RuntimeException {
        String query = searchParameter.getSearchQuery();

        switch (searchView.getSuggestionFragment().getCurrentTab()) {
            case SearchMainFragment.PAGER_POSITION_PRODUCT:
                onProductQuerySubmit();
                sendSearchProductGTM(query);
                break;
            case SearchMainFragment.PAGER_POSITION_SHOP:
                onShopQuerySubmit();
                sendSearchShopGTM(query);
                break;
            default:
                throw new RuntimeException("Please handle this function if you have new tab of suggestion search view.");
        }
    }

    protected void onProductQuerySubmit() {
        setForceSwipeToShop(false);
        moveToSearchPage();
    }

    private void onShopQuerySubmit() {
        setForceSwipeToShop(true);
        moveToSearchPage();
    }

    private void moveToSearchPage() {
        Intent searchActivityIntent = createIntentToSearchResult();

        startActivity(searchActivityIntent);
        setResult(AUTO_COMPLETE_ACTIVITY_RESULT_CODE_FINISH_ACTIVITY);
        finish();
    }

    private Intent createIntentToSearchResult() {
        Intent intent = RouteManager.getIntent(this, createSearchResultApplink());

        intent.putExtra(EXTRA_FORCE_SWIPE_TO_SHOP, isForceSwipeToShop());

        return intent;
    }

    private String createSearchResultApplink() {
        return ApplinkConstInternalDiscovery.SEARCH_RESULT
                + "?"
                + UrlParamHelper.generateUrlParamString(searchParameter.getSearchParameterHashMap());
    }

    private void sendSearchProductGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            eventDiscoverySearch(keyword);
        }
    }

    public void eventDiscoverySearch(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.EVENT_CLICK_TOP_NAV,
                SearchEventTracking.Category.EVENT_TOP_NAV,
                SearchEventTracking.Action.SEARCH_PRODUCT,
                label);
    }

    private void sendSearchShopGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            eventDiscoverySearchShop(keyword);
        }
    }

    public void eventDiscoverySearchShop(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.EVENT_CLICK_TOP_NAV,
                SearchEventTracking.Category.EVENT_TOP_NAV,
                SearchEventTracking.Action.SEARCH_SHOP,
                label);
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            eventDiscoveryVoiceSearch(keyword);
        }
    }

    public void eventDiscoveryVoiceSearch(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.SEARCH,
                SearchEventTracking.Category.SEARCH,
                SearchEventTracking.Action.VOICE_SEARCH,
                label);
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
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
    public void enableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(true);
        }
    }

    @Override
    public void disableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(false);
        }
    }

    @Override
    public void refreshBottomNavigationIcon(List<AHBottomNavigationItem> items) {
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
    }

    @Override
    public void onHandleResponseError() {

        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
        showLoadingView(false);
        showContainer(true);
        NetworkErrorHelper.showEmptyState(this, container, null);
    }

    @Override
    public void onImageSearchClicked() {
        RouteManager.route(this, ApplinkConstInternalDiscovery.IMAGE_SEARCH_RESULT);
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
