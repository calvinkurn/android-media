package com.tokopedia.discovery.newdiscovery.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.adapter.SearchSectionPagerAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.CatalogFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListFragment;
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;
import com.tokopedia.discovery.search.view.DiscoverySearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;
import static com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL;
import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class SearchActivity extends DiscoveryActivity
        implements SearchContract.View, RedirectionListener, DynamicFilterView {

    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";
    public static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";
    public static final String EXTRA_SELECTED_FLAG_FILTER = "EXTRA_SELECTED_FLAG_FILTER";

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String FILTER_SELECTED_CATEGORY_ROOT_ID_PREF = "filter_selected_category_root_id";
    public static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    public static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";

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

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;

    private RecyclerView recyclerView;
    private DynamicFilterAdapter adapter;
    private TextView buttonReset;
    private View buttonClose;
    private LinearLayout bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;

    private HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    private HashMap<String, String> savedTextInput = new HashMap<>();

    private int selectedExpandableItemPosition;
    private String selectedCategoryId;
    private String selectedCategoryName;
    private String selectedCategoryRootId;

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
        setPresenter(searchPresenter);
        searchPresenter.attachView(this);
        searchPresenter.setDiscoveryView(this);
        initResources();

        ProductViewModel productViewModel =
                getIntent().getParcelableExtra(EXTRA_PRODUCT_VIEW_MODEL);

        boolean forceSwipeToShop;
        String searchQuery = getIntent().getStringExtra(BrowseProductRouter.EXTRAS_SEARCH_TERM);

        if (savedInstanceState != null) {
            forceSwipeToShop = isForceSwipeToShop();
        } else {
            forceSwipeToShop = getIntent().getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false);
        }
        if (productViewModel != null) {
            setLastQuerySearchView(productViewModel.getQuery());
            loadSection(productViewModel, forceSwipeToShop);
            setToolbarTitle(productViewModel.getQuery());
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

        if (getIntent() != null &&
                getIntent().getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            UnifyTracking.eventBeliLongClick();
        }
        initFilterBottomSheet(savedInstanceState);
    }

    private void initFilterBottomSheet(Bundle savedInstanceState) {
        initRecyclerView();
        //loadLastFilterState(savedInstanceState);
    }

    private void loadLastFilterState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recoverLastFilterState(savedInstanceState);
        } else {
            loadLastFilterStateFromPreference();
        }
    }

    @SuppressWarnings("unchecked")
    private void recoverLastFilterState(Bundle savedInstanceState) {
        savedCheckedState = (HashMap<String, Boolean>) savedInstanceState.getSerializable(FILTER_CHECKED_STATE_PREF);
        savedTextInput = (HashMap<String, String>) savedInstanceState.getSerializable(FILTER_TEXT_PREF);
        selectedCategoryId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ID_PREF);
        selectedCategoryName = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_NAME_PREF);
        selectedCategoryRootId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ROOT_ID_PREF);
    }

    private void loadLastFilterStateFromPreference() {
        FilterFlagSelectedModel model = getIntent().getParcelableExtra(EXTRA_SELECTED_FLAG_FILTER);
        if (model != null) {
            savedCheckedState = model.getSavedCheckedState();
            savedTextInput = model.getSavedTextInput();
            selectedCategoryId = model.getCategoryId();
            selectedCategoryName = model.getSelectedCategoryName();
            selectedCategoryRootId = model.getSelectedCategoryRootId();
        }
    }

    private void initRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
        adapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    KeyboardHandler.hideSoftKeyboard(SearchActivity.this);
                }
            }
        });
    }

    public void launchFilterBottomSheet(List<Filter> filters) {
        loadFilterItems(filters);
        hideBottomNavigation();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void loadFilterItems(List<Filter> list) {
        removeFiltersWithEmptyOption(list);
        mergeSizeFilterOptionsWithSameValue(list);
        removeBrandFilterOptionsWithSameValue(list);
        adapter.setFilterList(list);
    }

    private void removeFiltersWithEmptyOption(List<Filter> filterList) {
        Iterator<Filter> iterator = filterList.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            if (filter.getOptions().isEmpty() && !filter.isSeparator()) {
                iterator.remove();
            }
        }
    }

    private void mergeSizeFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter sizeFilter = getSizeFilter(filterList);
        if (sizeFilter == null) {
            return;
        }

        List<Option> sizeFilterOptions = sizeFilter.getOptions();
        Iterator<Option> iterator = sizeFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                existingOption.setName(existingOption.getName() + " / " + getFormattedSizeName(option));
                iterator.remove();
            } else {
                option.setName(getFormattedSizeName(option));
                option.setMetric("");
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private Filter getSizeFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isSizeFilter()) return filter;
        }
        return null;
    }

    private void removeBrandFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter brandFilter = getBrandFilter(filterList);
        if (brandFilter == null) {
            return;
        }

        List<Option> brandFilterOptions = brandFilter.getOptions();
        Iterator<Option> iterator = brandFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                iterator.remove();
            } else {
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private Filter getBrandFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isBrandFilter()) return filter;
        }
        return null;
    }

    private String getFormattedSizeName(Option option) {
        if (METRIC_INTERNATIONAL.equals(option.getMetric())) {
            return option.getName();
        } else {
            return option.getName() + " " + option.getMetric();
        }
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
        SearchSectionPagerAdapter searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager());
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
        recyclerView = (RecyclerView) findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilterBottomSheet();
            }
        });
        buttonReset = (TextView) findViewById(R.id.top_bar_button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomSheetLayout = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void closeFilterBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            showBottomNavigation();
        }
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {

    }

    @Override
    public Boolean loadLastCheckedState(Option option) {
        return savedCheckedState.get(option.getUniqueId());
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked) {
        savedCheckedState.put(option.getUniqueId(), isChecked);
    }

    @Override
    public String removeSavedTextInput(String key) {
        return savedTextInput.remove(key);
    }

    @Override
    public void saveTextInput(String key, String textInput) {
        savedTextInput.put(key, textInput);
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        List<Option> selectedOptions = new ArrayList<>();

        if (filter.isCategoryFilter() && isCategorySelected()) {
            selectedOptions.add(getSelectedCategoryAsOption());
        } else {
            selectedOptions.addAll(getCheckedOptionList(filter));
        }
        return selectedOptions;
    }

    private boolean isCategorySelected() {
        return !TextUtils.isEmpty(selectedCategoryRootId) &&
                !TextUtils.isEmpty(selectedCategoryId) &&
                !TextUtils.isEmpty(selectedCategoryName);
    }

    private Option getSelectedCategoryAsOption() {
        return OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName);
    }

    private List<Option> getCheckedOptionList(Filter filter) {
        List<Option> checkedOptions = new ArrayList<>();

        for (Option option : filter.getOptions()) {
            if (Boolean.TRUE.equals(loadLastCheckedState(option))) {
                checkedOptions.add(option);
            }
        }
        return checkedOptions;
    }

    @Override
    public void removeSelectedOption(Option option) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            resetSelectedCategory();
        } else {
            saveCheckedState(option, false);
        }
    }

    private void resetSelectedCategory() {
        selectedCategoryId = null;
        selectedCategoryRootId = null;
        selectedCategoryName = null;
    }

    @Override
    public void updateLastRangeValue(int minValue, int maxValue) {
        Filter priceFilter = getPriceFilter();
        if (priceFilter == null) {
            return;
        }

        for (Option option : priceFilter.getOptions()) {
            if(Option.KEY_PRICE_MIN.equals(option.getKey())) {
                option.setValue(String.valueOf(minValue));
            } else if(Option.KEY_PRICE_MAX.equals(option.getKey())) {
                option.setValue(String.valueOf(maxValue));
            }
        }
    }

    private Filter getPriceFilter() {
        List<Filter> filterList = adapter.getFilterList();
        for (Filter filter : filterList) {
            if (filter.isPriceFilter()) return filter;
        }
        return null;
    }

    @Override
    protected void prepareView() {
        super.prepareView();

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                closeFilterBottomSheet();
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
}
