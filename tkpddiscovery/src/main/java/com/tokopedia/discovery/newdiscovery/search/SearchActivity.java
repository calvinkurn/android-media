package com.tokopedia.discovery.newdiscovery.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewTreeObserver;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
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

import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class SearchActivity extends DiscoveryActivity
        implements SearchContract.View, RedirectionListener {

    private static final String EXTRA_PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String EXTRA_FORCE_SWIPE_TO_SHOP = "FORCE_SWIPE_TO_SHOP";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;

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

        boolean forceSwipeToShop = getIntent().getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false);
        String searchQuery = getIntent().getStringExtra(BrowseProductRouter.EXTRAS_SEARCH_TERM);

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
        if (forceSwipeToShop) {
            swipeToShopWhenReady();
        }
    }

    private void swipeToShopWhenReady() {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewPager.setCurrentItem(getShopTabPosition());
            }
        });
    }

    private int getShopTabPosition() {
        return viewPager.getAdapter().getCount() - 1;
    }

    private void populateThreeTabItem(List<SearchSectionItem> searchSectionItemList,
                                      ProductViewModel productViewModel) {

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, getProductFragment(productViewModel)));
        searchSectionItemList.add(new SearchSectionItem(catalogTabTitle, getCatalogFragment(productViewModel.getQuery())));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, getShopFragment(productViewModel.getQuery())));
    }

    private Fragment getCatalogFragment(String query) {
        return CatalogFragment.createInstanceByQuery(query);
    }

    private Fragment getProductFragment(ProductViewModel productViewModel) {
        return ProductListFragment.newInstance(productViewModel);
    }

    private Fragment getShopFragment(String query) {
        return ShopListFragment.newInstance(query);
    }

    private void populateTwoTabItem(List<SearchSectionItem> searchSectionItemList,
                                    ProductViewModel productViewModel) {

        searchSectionItemList.add(new SearchSectionItem(productTabTitle, getProductFragment(productViewModel)));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, getShopFragment(productViewModel.getQuery())));
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
