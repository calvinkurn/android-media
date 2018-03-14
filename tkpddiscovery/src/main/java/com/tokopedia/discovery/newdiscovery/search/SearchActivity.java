package com.tokopedia.discovery.newdiscovery.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
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
import com.tokopedia.discovery.search.view.DiscoverySearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class SearchActivity extends DiscoveryActivity
        implements SearchContract.View, RedirectionListener {

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

    @Inject
    SearchPresenter searchPresenter;

    private SearchComponent searchComponent;
    private boolean forImageSearch = false;

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

            forImageSearch = productViewModel.isImageSearch();

            if (!forImageSearch)
                setToolbarTitle(productViewModel.getQuery());
            else
                setToolbarTitle("Image Search");
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
        } else if (!productViewModel.isImageSearch()) {
            populateTwoTabItem(searchSectionItemList, productViewModel);
        } else {
            populateOneTabItem(searchSectionItemList, productViewModel);
        }
        SearchSectionPagerAdapter searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager());
        searchSectionPagerAdapter.setData(searchSectionItemList);
        viewPager.setAdapter(searchSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setActiveTab(forceSwipeToShop);
    }

    private void populateOneTabItem(List<SearchSectionItem> searchSectionItemList, ProductViewModel productViewModel) {
        productListFragment = getProductFragment(productViewModel);
        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        tabLayout.setVisibility(View.GONE);
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

    public boolean isForImageSearch() {
        return forImageSearch;
    }

    public void setForImageSearch(boolean forImageSearch) {
        this.forImageSearch = forImageSearch;
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
    }

    @Override
    protected void prepareView() {
        super.prepareView();

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
