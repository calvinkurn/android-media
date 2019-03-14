package com.tokopedia.discovery.imagesearch.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.search.view.DiscoverySearchView;

import javax.inject.Inject;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchActivity extends DiscoveryActivity
        implements ImageSearchContract.View, RedirectionListener {


    private static final String EXTRA_PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    @Inject
    ImageSearchPresenter searchPresenter;

    private SearchComponent searchComponent;

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ImageSearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }


    public static void moveTo(AppCompatActivity activity,
                              ProductViewModel productViewModel) {
        if (activity != null) {
            Intent intent = new Intent(activity, ImageSearchActivity.class);
            intent.putExtra(EXTRA_PRODUCT_VIEW_MODEL, productViewModel);
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

        ProductViewModel productViewModel =
                getIntent().getParcelableExtra(EXTRA_PRODUCT_VIEW_MODEL);

        String searchQuery = getIntent().getStringExtra(BrowseProductRouter.EXTRAS_SEARCH_TERM);

        if (productViewModel != null) {
            setLastQuerySearchView(productViewModel.getQuery());
            loadSection(productViewModel);

            setToolbarTitle(getString(R.string.image_search_title));
        } else if (!TextUtils.isEmpty(searchQuery)) {
            onProductQuerySubmit(searchQuery,false);
        } else {
            searchView.showSearch(true, false);
        }

        super.initView();
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();

        searchComponent.inject(this);
    }

    private void loadSection(ProductViewModel productViewModel) {
        addFragment(R.id.container, ImageSearchProductListFragment.newInstance(productViewModel));
    }

    private void addFragment(int containerViewId, ImageSearchProductListFragment fragment) {
        if (!isFinishing() && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected int getLayoutRes() {
        return super.getLayoutRes();
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
