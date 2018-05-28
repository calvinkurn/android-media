package com.tokopedia.discovery.newdiscovery.search;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewTreeObserver;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.SearchTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.adapter.SearchSectionPagerAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.CatalogFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListFragment;
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem;
import com.tokopedia.discovery.newdiscovery.widget.BottomSheetFilterView;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.search.view.DiscoverySearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;

/**
 * Created by henrypriyono on 10/6/17.
 */

@RuntimePermissions
public class SearchActivity extends DiscoveryActivity
        implements SearchContract.View, RedirectionListener, BottomSheetListener {

    public static final int TAB_THIRD_POSITION = 2;
    public static final int TAB_SECOND_POSITION = 1;
    public static final int TAB_PRODUCT = 0;

    private static final String EXTRA_PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String EXTRA_FORCE_SWIPE_TO_SHOP = "FORCE_SWIPE_TO_SHOP";

    private ProductListFragment productListFragment;
    private CatalogFragment catalogFragment;
    private ShopListFragment shopListFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchSectionPagerAdapter searchSectionPagerAdapter;

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;
    private boolean forceSwipeToShop;

    private BottomSheetFilterView bottomSheetFilterView;

    @Inject
    SearchPresenter searchPresenter;

    private SearchComponent searchComponent;

    public SearchComponent getSearchComponent() {
        return searchComponent;
    }

    @DeepLink(Constants.Applinks.DISCOVERY_SEARCH)
    public static Intent getCallingApplinkSearchIntent(Context context, Bundle bundle) {
        String departmentId = bundle.getString(BrowseApi.SC);
        Intent intent = new Intent(context, SearchActivity.class);

        if (!TextUtils.isEmpty(departmentId)) {
            intent = BrowseProductRouter.getDefaultBrowseIntent(context);
            throw new RuntimeException("this should go to category activity");
        }

        intent.putExtra(EXTRAS_SEARCH_TERM, bundle.getString(BrowseApi.Q, bundle.getString("keyword", "")));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static void moveTo(AppCompatActivity activity,
                              ProductViewModel productViewModel,
                              boolean forceSwipeToShop) {
        if (activity != null) {
            Intent intent = new Intent(activity, SearchActivity.class);
            intent.putExtra(EXTRA_PRODUCT_VIEW_MODEL, productViewModel);
            intent.putExtra(EXTRA_FORCE_SWIPE_TO_SHOP, forceSwipeToShop);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();

        if (savedInstanceState != null) {
            forceSwipeToShop = isForceSwipeToShop();
        } else {
            forceSwipeToShop = getIntent().getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false);
        }
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        setPresenter(searchPresenter);
        searchPresenter.attachView(this);
        searchPresenter.setDiscoveryView(this);
        initResources();
        ProductViewModel productViewModel =
                intent.getParcelableExtra(EXTRA_PRODUCT_VIEW_MODEL);

        String searchQuery = intent.getStringExtra(BrowseProductRouter.EXTRAS_SEARCH_TERM);

        if (productViewModel != null) {
            setLastQuerySearchView(productViewModel.getQuery());
            loadSection(productViewModel, forceSwipeToShop);
            setToolbarTitle(productViewModel.getQuery());
            bottomSheetFilterView.setFilterResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
        } else if (!TextUtils.isEmpty(searchQuery)) {
            onProductQuerySubmit(searchQuery);
        } else {
            searchView.showSearch(true, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    KeyboardHandler.showSoftKeyboard(SearchActivity.this);
                }
            }, 200);
        }

        if (intent != null &&
                intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            UnifyTracking.eventBeliLongClick();
        }
        bottomSheetFilterView.initFilterBottomSheet(savedInstanceState);

        handleImageUri(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleImageUri(Intent intent) {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SHOW_IMAGE_SEARCH, false) &&
                intent != null) {

            if (intent.getClipData() != null &&
                    intent.getClipData().getItemCount() > 0) {

                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                ClipData clipData = intent.getClipData();
                Uri uri = clipData.getItemAt(0).getUri();
                SearchActivityPermissionsDispatcher.onImageSuccessWithCheck(SearchActivity.this, uri.toString());
            } else if (intent.getData() != null &&
                    !TextUtils.isEmpty(intent.getData().toString())) {
                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                SearchActivityPermissionsDispatcher.onImageSuccessWithCheck(SearchActivity.this, intent.getData().toString());
            }
        }
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onImageSuccess(String uri) {
        onImagePickedSuccess(uri);
    }


    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SearchActivityPermissionsDispatcher.onRequestPermissionsResult(
                SearchActivity.this, requestCode, grantResults);

    }

    private void sendImageSearchFromGalleryGTM(String label) {
        UnifyTracking.eventDiscoveryExternalImageSearch(label);
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();

        searchComponent.inject(this);
    }

    private void initResources() {
        productTabTitle = getString(R.string.product_tab_title);
        catalogTabTitle = getString(R.string.catalog_tab_title);
        shopTabTitle = getString(R.string.shop_tab_title);
    }

    private void loadSection(ProductViewModel productViewModel, boolean forceSwipeToShop) {

        List<SearchSectionItem> searchSectionItemList = new ArrayList<>();

        if (productViewModel.isHasCatalog()) {
            populateThreeTabItem(searchSectionItemList, productViewModel);
        } else {
            populateTwoTabItem(searchSectionItemList, productViewModel);
        }
        searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager());
        searchSectionPagerAdapter.setData(searchSectionItemList);
        viewPager.setAdapter(searchSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setActiveTab(forceSwipeToShop);
    }

    private void setActiveTab(final boolean swipeToShop) {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (swipeToShop) {
                    viewPager.setCurrentItem(getShopTabPosition());
                } else {
                    viewPager.setCurrentItem(getActiveTabPosition());
                }
            }
        });
    }

    private int getShopTabPosition() {
        return viewPager.getAdapter().getCount() - 1;
    }

    private void populateThreeTabItem(List<SearchSectionItem> searchSectionItemList,
                                      ProductViewModel productViewModel) {

        productListFragment = getProductFragment(productViewModel);
        catalogFragment = getCatalogFragment(productViewModel.getQuery());
        shopListFragment = getShopFragment(productViewModel.getQuery());

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        searchSectionItemList.add(new SearchSectionItem(catalogTabTitle, catalogFragment));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, shopListFragment));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_PRODUCT:
                        productListFragment.backToTop();
                        break;
                    case TAB_SECOND_POSITION:
                        catalogFragment.backToTop();
                        break;
                    case TAB_THIRD_POSITION:
                        shopListFragment.backToTop();
                        break;
                }
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_PRODUCT:
                        SearchTracking.eventSearchResultTabClick(productTabTitle);
                        break;
                    case TAB_SECOND_POSITION:
                        SearchTracking.eventSearchResultTabClick(catalogTabTitle);
                        break;
                    case TAB_THIRD_POSITION:
                        SearchTracking.eventSearchResultTabClick(shopTabTitle);
                        break;
                }
            }
        });
    }

    private CatalogFragment getCatalogFragment(String query) {
        return CatalogFragment.createInstanceByQuery(query);
    }

    private ProductListFragment getProductFragment(ProductViewModel productViewModel) {
        return ProductListFragment.newInstance(productViewModel);
    }

    private ShopListFragment getShopFragment(String query) {
        return ShopListFragment.newInstance(query);
    }

    private void populateTwoTabItem(List<SearchSectionItem> searchSectionItemList,
                                    ProductViewModel productViewModel) {

        productListFragment = getProductFragment(productViewModel);
        shopListFragment = getShopFragment(productViewModel.getQuery());

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, shopListFragment));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_PRODUCT:
                        productListFragment.backToTop();
                        break;
                    case TAB_SECOND_POSITION:
                        shopListFragment.backToTop();
                        break;
                }
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_PRODUCT:
                        SearchTracking.eventSearchResultTabClick(productTabTitle);
                        break;
                    case TAB_SECOND_POSITION:
                        SearchTracking.eventSearchResultTabClick(shopTabTitle);
                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        super.initView();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        bottomSheetFilterView = (BottomSheetFilterView) findViewById(R.id.bottomSheetFilter);
    }

    @Override
    protected void prepareView() {
        super.prepareView();

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomSheetFilterView.closeView();
            }

            @Override
            public void onPageSelected(int position) {
                setForceSwipeToShop(false);
                setActiveTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initBottomSheetListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomSheetFilterView.onActivityResult(requestCode, resultCode, data);
    }

    private void initBottomSheetListener() {
        bottomSheetFilterView.setCallback(new BottomSheetFilterView.Callback() {
            @Override
            public void onApplyFilter(HashMap<String, String> selectedFilter,
                                      FilterFlagSelectedModel filterFlagSelectedModel) {
                applyFilter(selectedFilter, filterFlagSelectedModel);
            }

            @Override
            public void onShow() {
                hideBottomNavigation();
                disableAutoShowBottomNav();
            }

            @Override
            public void onHide() {
                enableAutoShowBottomNav();
                forceShowBottomNav();
                sendBottomSheetHideEventForProductList();
            }

            @Override
            public boolean isSearchShown() {
                return searchView.isSearchOpen();
            }

            @Override
            public void hideKeyboard() {
                KeyboardHandler.hideSoftKeyboard(SearchActivity.this);
            }

            @Override
            public void launchFilterCategoryPage(Filter filter, String selectedCategoryRootId, String selectedCategoryId) {
                SearchTracking.eventSearchResultNavigateToFilterDetail(getResources().getString(R.string.title_category));
                FilterDetailActivityRouter.launchCategoryActivity(SearchActivity.this,
                        filter, selectedCategoryRootId, selectedCategoryId, true);
            }

            @Override
            public void launchFilterDetailPage(Filter filter) {
                SearchTracking.eventSearchResultNavigateToFilterDetail(filter.getTitle());
                FilterDetailActivityRouter.launchDetailActivity(SearchActivity.this, filter, true);
            }
        });
    }

    private void forceShowBottomNav() {
        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        if (selectedFragment != null) {
            selectedFragment.showBottomBarNavigation(true);
        }
    }

    private void sendBottomSheetHideEventForProductList() {
        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        if (selectedFragment != null && selectedFragment instanceof ProductListFragment) {
            selectedFragment.onBottomSheetHide();
        }
    }

    @Override
    public void performNewProductSearch(String query, boolean forceSearch) {
        setForceSearch(forceSearch);
        setForceSwipeToShop(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query);
    }

    @Override
    public void showSearchInputView() {
        searchView.showSearch(true, DiscoverySearchView.TAB_DEFAULT_SUGGESTION);
        searchView.setFinishOnClose(false);
    }

    @Override
    protected void onDestroy() {
        searchPresenter.detachView();
        super.onDestroy();
    }

    private void applyFilter(HashMap<String, String> selectedFilter,
                             FilterFlagSelectedModel filterFlagSelectedModel) {

        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        selectedFragment.setSelectedFilter(selectedFilter);
        selectedFragment.setFlagFilterHelper(filterFlagSelectedModel);
        selectedFragment.clearDataFilterSort();
        selectedFragment.reloadData();
    }

    @Override
    public void onBackPressed() {
        if (!bottomSheetFilterView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSearchViewShown() {
        super.onSearchViewShown();
        bottomSheetFilterView.closeView();
    }

    @Override
    public void loadFilterItems(ArrayList<Filter> filters, FilterFlagSelectedModel filterFlagSelectedModel) {
        bottomSheetFilterView.loadFilterItems(filters, filterFlagSelectedModel);
    }

    @Override
    public void setFilterResultCount(String formattedResultCount) {
        bottomSheetFilterView.setFilterResultCount(formattedResultCount);
    }

    @Override
    public void closeFilterBottomSheet() {
        bottomSheetFilterView.closeView();
    }

    @Override
    public boolean isBottomSheetShown() {
        return bottomSheetFilterView.isBottomSheetShown();
    }

    @Override
    public void launchFilterBottomSheet() {
        bottomSheetFilterView.launchFilterBottomSheet();
    }
}