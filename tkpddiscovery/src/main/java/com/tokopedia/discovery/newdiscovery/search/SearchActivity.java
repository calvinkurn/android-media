package com.tokopedia.discovery.newdiscovery.search;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
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
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListFragment;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem;
import com.tokopedia.discovery.newdiscovery.widget.BottomSheetFilterView;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;

/**
 * Created by henrypriyono on 10/6/17.
 */

@RuntimePermissions
public class SearchActivity extends DiscoveryActivity
        implements SearchContract.View,
                    RedirectionListener,
                    BottomSheetListener,
                    SearchNavigationListener {

    public static final int TAB_THIRD_POSITION = 2;
    public static final int TAB_SECOND_POSITION = 1;
    public static final int TAB_PRODUCT = 0;

    private static final String DEEP_LINK_URI = "deep_link_uri";
    private static final String EXTRA_PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String EXTRA_FORCE_SWIPE_TO_SHOP = "FORCE_SWIPE_TO_SHOP";
    private static final String EXTRA_ACTIVITY_PAUSED = "EXTRA_ACTIVITY_PAUSED";
    private static final String EXTRA_OFFICIAL = "EXTRA_OFFICIAL";
    private static final String EXTRA_IS_AUTOCOMPLETE= "EXTRA_IS_AUTOCOMPLETE";
    private static final String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    private ProductListFragment productListFragment;
    private CatalogFragment catalogFragment;
    private ShopListFragment shopListFragment;
    private ProfileListFragment profileListFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchSectionPagerAdapter searchSectionPagerAdapter;
    private TextView buttonFilter;
    private TextView buttonSort;
    private View searchNavDivider;
    private View searchNavContainer;

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;
    private String profileTabTitle;
    private boolean forceSwipeToShop;
    private BottomSheetFilterView bottomSheetFilterView;
    private SearchNavigationListener.ClickListener searchNavigationClickListener;
    private boolean isHandlingIntent;

    @Inject
    SearchPresenter searchPresenter;

    @Inject
    SearchTracking searchTracking;

    private SearchComponent searchComponent;
    private MenuItem menuChangeGrid;

    public SearchComponent getSearchComponent() {
        return searchComponent;
    }

    @DeepLink(ApplinkConst.DISCOVERY_SEARCH)
    public static Intent getCallingApplinkSearchIntent(Context context, Bundle bundle) {
        return createIntentToSearchActivityFromBundle(context, bundle);
    }

    @DeepLink(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
    public static Intent getCallingApplinkAutoCompleteSearchIntent(Context context, Bundle bundle) {
        Intent intent = createIntentToSearchActivityFromBundle(context, bundle);

        intent.putExtra(EXTRA_IS_AUTOCOMPLETE, true);

        return intent;
    }

    private static Intent createIntentToSearchActivityFromBundle(Context context, Bundle bundle) {
        SearchParameter searchParameter = createSearchParameterFromBundle(bundle);

        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, searchParameter);

        return intent;
    }

    private static SearchParameter createSearchParameterFromBundle(Bundle bundle) {
        String deepLinkURI = bundle.getString(DEEP_LINK_URI);
        return new SearchParameter(deepLinkURI == null ? "" : deepLinkURI);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();

        if(!isHandlingIntent && !hasSearchData()) {
            showAutoCompleteOnResume();
        }
    }

    private boolean hasSearchData() {
        return searchSectionPagerAdapter != null
                && searchSectionPagerAdapter.getCount() > 0;
    }

    private void showAutoCompleteOnResume() {
        if(searchView.isSearchOpen()) {
            searchView.searchTextViewRequestFocus();
            searchView.searchTextViewSetCursorSelectionAtTextEnd();
            forceShowKeyBoard();
        }
        else {
            searchView.showSearch(true, false);
        }
    }

    private void forceShowKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static void moveTo(AppCompatActivity activity,
                              ProductViewModel productViewModel,
                              boolean forceSwipeToShop,
                              boolean isActivityPaused) {
        if (activity != null) {
            // Set empty DynamicFilterModel as temporary solution for TransactionTooLargeException
            // Dynamic Filter Model will be loaded inside ProductListFragment for now
            // For long term solution, ProductViewModel will not be sent from SearchActivity, but should be loaded inside ProductListFragment
            productViewModel.setDynamicFilterModel(new DynamicFilterModel());

            Intent intent = new Intent(activity, SearchActivity.class);
            intent.putExtra(EXTRA_PRODUCT_VIEW_MODEL, productViewModel);
            intent.putExtra(EXTRA_FORCE_SWIPE_TO_SHOP, forceSwipeToShop);
            intent.putExtra(EXTRA_ACTIVITY_PAUSED, isActivityPaused);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityOnCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    private void initActivityOnCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        initInjector();
        initForceSwipeToShop(savedInstanceState);
        bottomSheetFilterView.initFilterBottomSheet();
    }

    private void initForceSwipeToShop(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            forceSwipeToShop = isForceSwipeToShop();
        } else {
            forceSwipeToShop = getIntent().getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        isHandlingIntent = true;

        initPresenter();
        initResources();

        boolean isAutoComplete = intent.getBooleanExtra(EXTRA_IS_AUTOCOMPLETE, false);
        SearchParameter searchParameter = getSearchParameterFromIntent(intent);

        if(isAutoComplete) {
            handleIntentAutoComplete(searchParameter);
        }
        else {
            handleIntentSearch(intent, searchParameter);
        }

        if (intent != null &&
                intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            searchTracking.eventSearchShortcut();
        }

        handleImageUri(intent);
    }

    private SearchParameter getSearchParameterFromIntent(Intent intent) {
        SearchParameter searchParameter = intent.getParcelableExtra(EXTRA_SEARCH_PARAMETER_MODEL);

        if(searchParameter == null) {
            searchParameter = new SearchParameter();
        }

        return searchParameter;
    }

    private void handleIntentAutoComplete(SearchParameter searchParameter) {
        searchView.showSearch(true, false, searchParameter);
        isHandlingIntent = false;
    }

    private void handleIntentSearch(Intent intent, SearchParameter searchParameter) {
        ProductViewModel productViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VIEW_MODEL);

        handleIntentActivityPaused();

        if (productViewModel != null) {
            handleIntentWithProductViewModel(productViewModel);
        } else if (!TextUtils.isEmpty(searchParameter.getSearchQuery())) {
            handleIntentWithSearchQuery(searchParameter);
        } else {
            handleIntentAutoComplete(searchParameter);
        }
    }

    private void handleIntentActivityPaused() {
        if (getIntent().getBooleanExtra(EXTRA_ACTIVITY_PAUSED, false)) {
            moveTaskToBack(true);
        }
    }

    private void handleIntentWithProductViewModel(ProductViewModel productViewModel) {
        this.searchParameter = productViewModel.getSearchParameter();

        setLastQuerySearchView(productViewModel.getQuery());
        loadSection(productViewModel, forceSwipeToShop);
        setToolbarTitle(productViewModel.getQuery());
        bottomSheetFilterView.setFilterResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());

        isHandlingIntent = false;
    }

    private void handleIntentWithSearchQuery(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
        onProductQuerySubmit();
    }

    private void handleImageUri(Intent intent) {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        if (remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH, false) &&
                intent != null) {

            if (intent.getClipData() != null &&
                    intent.getClipData().getItemCount() > 0) {

                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                ClipData clipData = intent.getClipData();
                Uri uri = clipData.getItemAt(0).getUri();
                onImageSuccess(uri.toString());
            } else if (intent.getData() != null &&
                    !TextUtils.isEmpty(intent.getData().toString()) &&
                    isValidMimeType(intent.getData().toString())) {
                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                onImageSuccess(intent.getData().toString());
            }
        }
    }

    private boolean isValidMimeType(String url) {
        String mimeType = getMimeTypeUri(Uri.parse(url));

        return mimeType != null &&
                (mimeType.equalsIgnoreCase("image/jpg") ||
                        mimeType.equalsIgnoreCase("image/png") ||
                        mimeType.equalsIgnoreCase("image/jpeg"));

    }

    private String getMimeTypeUri(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
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
        onRequestPermissionsResult(
                requestCode, permissions, grantResults);

    }

    private void sendImageSearchFromGalleryGTM(String label) {
        UnifyTracking.eventDiscoveryExternalImageSearch(this, label);
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();
        searchComponent.inject(this);
    }

    private void initPresenter() {
        setPresenter(searchPresenter);
        searchPresenter.attachView(this);
        searchPresenter.setDiscoveryView(this);
    }

    private void initResources() {
        productTabTitle = getString(R.string.product_tab_title);
        catalogTabTitle = getString(R.string.catalog_tab_title);
        shopTabTitle = getString(R.string.shop_tab_title);
        profileTabTitle = getString(R.string.title_profile);
    }

    private void loadSection(ProductViewModel productViewModel, boolean forceSwipeToShop) {

        List<SearchSectionItem> searchSectionItemList = new ArrayList<>();

        if (productViewModel.isHasCatalog()) {
            populateFourTabItem(searchSectionItemList, productViewModel);
        } else {
            populateThreeTabItem(searchSectionItemList, productViewModel);
        }
        searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager());
        searchSectionPagerAdapter.setData(searchSectionItemList);
        viewPager.setAdapter(searchSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setActiveTab(forceSwipeToShop, productViewModel.isHasCatalog());
    }

    private void setActiveTab(final boolean swipeToShop, final boolean hasCatalogTab) {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (swipeToShop) {
                    viewPager.setCurrentItem(getShopTabPosition(hasCatalogTab));
                } else {
                    viewPager.setCurrentItem(getActiveTabPosition());
                }
            }
        });
    }

    private int getShopTabPosition(boolean hasCatalogTab) {
        return hasCatalogTab ? 2 : 1;
    }

    private void populateFourTabItem(List<SearchSectionItem> searchSectionItemList,
                                      ProductViewModel productViewModel) {

        productListFragment = getProductFragment(productViewModel);
        catalogFragment = getCatalogFragment();
        shopListFragment = getShopFragment();
        profileListFragment = getProfileListFragment(productViewModel.getQuery(), this);

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        searchSectionItemList.add(new SearchSectionItem(catalogTabTitle, catalogFragment));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, shopListFragment));
        searchSectionItemList.add(new SearchSectionItem(getString(R.string.title_profile), profileListFragment));

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
                        SearchTracking.eventSearchResultTabClick(getActivityContext(), productTabTitle);
                        break;
                    case TAB_SECOND_POSITION:
                        SearchTracking.eventSearchResultTabClick(getActivityContext(), catalogTabTitle);
                        break;
                    case TAB_THIRD_POSITION:
                        SearchTracking.eventSearchResultTabClick(getActivityContext(), shopTabTitle);
                        break;
                }
            }
        });
    }

    private CatalogFragment getCatalogFragment() {
        return CatalogFragment.newInstance(searchParameter);
    }

    private ProductListFragment getProductFragment(ProductViewModel productViewModel) {
        return ProductListFragment.newInstance(productViewModel);
    }

    private ShopListFragment getShopFragment() {
        return ShopListFragment.newInstance(searchParameter);
    }

    private ProfileListFragment getProfileListFragment(String query, SearchNavigationListener searchNavigationListener) {
        return ProfileListFragment.Companion.newInstance(query, searchNavigationListener, this);
    }

    private void populateThreeTabItem(List<SearchSectionItem> searchSectionItemList,
                                      ProductViewModel productViewModel) {

        productListFragment = getProductFragment(productViewModel);
        shopListFragment = getShopFragment();
        profileListFragment = getProfileListFragment(productViewModel.getQuery(), this);

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, shopListFragment));
        searchSectionItemList.add(new SearchSectionItem(getString(R.string.title_profile), profileListFragment));

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
                        SearchTracking.eventSearchResultTabClick(getActivityContext(), productTabTitle);
                        break;
                    case TAB_SECOND_POSITION:
                        SearchTracking.eventSearchResultTabClick(getActivityContext(), shopTabTitle);
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
        buttonFilter = findViewById(R.id.button_filter);
        buttonSort = findViewById(R.id.button_sort);
        searchNavDivider = findViewById(R.id.search_nav_divider);
        searchNavContainer = findViewById(R.id.search_nav_container);
    }

    @Override
    protected void prepareView() {
        super.prepareView();

        viewPager.setOffscreenPageLimit(3);
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
        initSearchNavigationListener();
    }

    private void initSearchNavigationListener() {
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchNavigationClickListener != null) {
                    searchNavigationClickListener.onFilterClick();
                }
            }
        });
        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchNavigationClickListener != null) {
                    searchNavigationClickListener.onSortClick();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomSheetFilterView.onActivityResult(requestCode, resultCode, data);
    }

    private void initBottomSheetListener() {
        bottomSheetFilterView.setCallback(new BottomSheetFilterView.Callback() {
            @Override
            public void onApplyFilter(Map<String, String> filterParameter) {
                applyFilter(filterParameter);
            }

            @Override
            public void onShow() {
                hideBottomNavigation();
            }

            @Override
            public void onHide() {
                showBottomNavigation();
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
                SearchTracking.eventSearchResultNavigateToFilterDetail(getActivityContext(), getResources().getString(R.string.title_category));
                FilterDetailActivityRouter.launchCategoryActivity(SearchActivity.this,
                        filter, selectedCategoryRootId, selectedCategoryId, true);
            }

            @Override
            public void launchFilterDetailPage(Filter filter) {
                SearchTracking.eventSearchResultNavigateToFilterDetail(getActivityContext(), filter.getTitle());
                FilterDetailActivityRouter.launchDetailActivity(SearchActivity.this, filter, true);
            }
        });
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
        searchParameter = new SearchParameter();

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

    private void applyFilter(Map<String, String> filterParameter) {
        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        selectedFragment.applyFilterToSearchParameter(filterParameter);
        selectedFragment.setSelectedFilter(new HashMap<>(filterParameter));
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
    public void loadFilterItems(ArrayList<Filter> filters, Map<String, String> searchParameter) {
        bottomSheetFilterView.loadFilterItems(filters, searchParameter);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomSheetFilterView.closeView();
    }

    private Context getActivityContext() {
        return this;
    }

    @Override
    public void setupSearchNavigation(ClickListener clickListener, boolean isSortEnabled) {
        showBottomNavigation();
        if (isSortEnabled) {
            buttonSort.setVisibility(View.VISIBLE);
            searchNavDivider.setVisibility(View.VISIBLE);
        } else {
            buttonSort.setVisibility(View.GONE);
            searchNavDivider.setVisibility(View.GONE);
        }
        this.searchNavigationClickListener = clickListener;
    }

    @Override
    public void refreshMenuItemGridIcon(int titleResId, int iconResId) {
        if (menuChangeGrid != null) {
            menuChangeGrid.setIcon(iconResId);
            menuChangeGrid.setTitle(titleResId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_grid, menu);
        menuChangeGrid = menu.findItem(R.id.action_change_grid);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_grid) {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onChangeGridClick();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBottomNavigation() {
        searchNavContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigation() {
        searchNavContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onSearchingStart(String keyword) {
        super.onSearchingStart(keyword);
        hideBottomNavigation();
    }
}