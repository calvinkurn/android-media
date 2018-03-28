package com.tokopedia.discovery.newdiscovery.category.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.category.di.component.CategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.di.component.DaggerCategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategorySectionItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.base.CategorySectionPagerAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.ProductFragment;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.CatalogFragment;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class CategoryActivity extends DiscoveryActivity implements CategoryContract.View {

    private static final String EXTRA_CATEGORY_HEADER_VIEW_MODEL = "CATEGORY_HADES_MODEL";
    private static final String EXTRA_TRACKER_ATTRIBUTION = "EXTRA_TRACKER_ATTRIBUTION";

    public static final int TAB_SHOP_CATALOG= 1;
    public static final int TAB_PRODUCT = 0;

    private String departmentId;
    private String categoryName;
    private String categoryUrl;
    private String trackerAttribution;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FrameLayout container;

    private  ProductFragment productFragment;
    private CatalogFragment catalogFragment;

    @Inject
    CategoryPresenter categoryPresenter;

    private CategoryComponent categoryComponent;

    public static void moveTo(Context context, String departmentId, String categoryName, boolean removeAnimation, String trackerAttribution) {
        if (context != null) {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra(BrowseProductRouter.DEPARTMENT_ID, departmentId);
            intent.putExtra(BrowseProductRouter.DEPARTMENT_NAME, categoryName);
            intent.putExtra(EXTRA_TRACKER_ATTRIBUTION, trackerAttribution);
            if (removeAnimation) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }
    }

    public static void moveToDestroyIntermediary(
            FragmentActivity activity, String departmentId, String categoryName, boolean removeAnimation) {
        if (activity != null) {
            Intent intent = new Intent(activity, CategoryActivity.class);
            intent.putExtra(BrowseProductRouter.DEPARTMENT_ID, departmentId);
            intent.putExtra(BrowseProductRouter.DEPARTMENT_NAME, categoryName);
            if (removeAnimation) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivityForResult(intent,CategoryNavigationActivity.DESTROY_INTERMEDIARY);
        }
    }

    public static void moveTo(Context context,
                              CategoryHeaderModel categoryHeaderModel,
                              boolean removeAnimation,
                              String trackerAttribution) {
        if (context != null) {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY_HEADER_VIEW_MODEL, categoryHeaderModel);
            intent.putExtra(EXTRA_TRACKER_ATTRIBUTION, trackerAttribution);
            if (removeAnimation) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }
    }

    public static void moveTo(Context context, String categoryUrl) {
        if (context != null) {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra(BrowseProductRouter.EXTRA_CATEGORY_URL, categoryUrl);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        setPresenter(categoryPresenter);
        categoryPresenter.attachView(this);
        categoryPresenter.setDiscoveryView(this);
        categoryName = "";
        loadInitialData();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_category;
    }

    @Override
    protected void initView() {
        super.initView();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        container = (FrameLayout) findViewById(R.id.container);
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        viewPager.setOffscreenPageLimit(1);
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
    public void showLoading() {
        showLoadingView(true);
    }

    @Override
    public void hideLoading() {
        showLoadingView(false);
    }

    private void loadInitialData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.getParcelable(EXTRA_CATEGORY_HEADER_VIEW_MODEL)!=null) {
            CategoryHeaderModel categoryHeaderModel = bundle.getParcelable(EXTRA_CATEGORY_HEADER_VIEW_MODEL);
            trackerAttribution = bundle.getString(EXTRA_TRACKER_ATTRIBUTION, "");
            departmentId = categoryHeaderModel.getDepartementId();
            categoryName = categoryHeaderModel.getHeaderModel().getCategoryName();
            categoryPresenter.getCategoryPage1(categoryHeaderModel);
        } else if (!TextUtils.isEmpty(bundle.getString(BrowseProductRouter.DEPARTMENT_ID))){
            trackerAttribution = bundle.getString(EXTRA_TRACKER_ATTRIBUTION, "");
            departmentId = bundle.getString(BrowseProductRouter.DEPARTMENT_ID);
            if (!TextUtils.isEmpty(bundle.getString(BrowseProductRouter.DEPARTMENT_NAME))) {
                categoryName = bundle.getString(BrowseProductRouter.DEPARTMENT_NAME);
            }
            categoryPresenter.getCategoryHeader(departmentId,new HashMap<String, String>());
        } else if (!TextUtils.isEmpty(bundle.getString(BrowseProductRouter.EXTRA_CATEGORY_URL))){
            trackerAttribution = bundle.getString(EXTRA_TRACKER_ATTRIBUTION, "");
            categoryUrl = bundle.getString(BrowseProductRouter.EXTRA_CATEGORY_URL);
            URLParser urlParser = new URLParser(categoryUrl);
            departmentId = urlParser.getDepIDfromURI(CategoryActivity.this);
            categoryPresenter.getCategoryHeader(departmentId,urlParser.getParamKeyValueMap());
        }
        setToolbarTitle(categoryName);
    }

    @Override
    public void prepareFragment(ProductViewModel productViewModel) {
        List<CategorySectionItem> categorySectionItems = new ArrayList<>();
        if (!TextUtils.isEmpty(categoryUrl)) {
            productFragment = ProductFragment.newInstance(productViewModel, categoryUrl, trackerAttribution);
        } else {
            productFragment = ProductFragment.newInstance(productViewModel, trackerAttribution);
        }

        if (productViewModel.isHasCatalog()) {
            catalogFragment = getCatalogFragment(
                    productViewModel.getCategoryHeaderModel().getDepartementId());
            categorySectionItems.add(new CategorySectionItem(
                    getResources().getString(R.string.product_tab_title), productFragment));
            categorySectionItems.add(new CategorySectionItem(
                    getResources().getString(R.string.catalog_tab_title), getCatalogFragment(
                    productViewModel.getCategoryHeaderModel().getDepartementId())));
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            categorySectionItems.add(new CategorySectionItem(
                    getResources().getString(R.string.product_tab_title), productFragment));
            tabLayout.setVisibility(View.GONE);

        }
        CategorySectionPagerAdapter categorySectionPagerAdapter = new CategorySectionPagerAdapter(getSupportFragmentManager());
        categorySectionPagerAdapter.setData(categorySectionItems);
        viewPager.setAdapter(categorySectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setToolbarTitle(productViewModel.getCategoryHeaderModel().getHeaderModel().getCategoryName());
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_PRODUCT:
                        productFragment.backToTop();
                        break;
                    case TAB_SHOP_CATALOG:
                        catalogFragment.backToTop();
                        break;


                }

            }
        });
    }

    private void initInjector() {
        categoryComponent =
                DaggerCategoryComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();

        categoryComponent.inject(this);
    }

    private CatalogFragment getCatalogFragment(String departmentId) {
        return CatalogFragment.createInstanceByCategoryID(departmentId);
    }

    private Fragment getProductFragment(ProductViewModel productViewModel) {
        return ProductFragment.newInstance(productViewModel, trackerAttribution);
    }
    protected void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onHandleResponseError() {
        showLoadingView(false);
        showContainer(true);
        NetworkErrorHelper.showEmptyState(this, container, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadInitialData();
            }
        });
        hideBottomNavigation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == CategoryNavigationActivity.DESTROY_BROWSE_PARENT) {
            setResult(CategoryNavigationActivity.DESTROY_INTERMEDIARY);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryPresenter.detachView();
    }
}
