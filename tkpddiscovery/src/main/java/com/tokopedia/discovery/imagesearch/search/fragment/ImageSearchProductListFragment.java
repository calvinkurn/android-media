package com.tokopedia.discovery.imagesearch.search.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;

import java.util.List;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchProductListFragment extends SearchSectionFragment {

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";

    public static ImageSearchProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        ImageSearchProductListFragment imageSearchProductListFragment = new ImageSearchProductListFragment();
        imageSearchProductListFragment.setArguments(args);
        return imageSearchProductListFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public String getScreenNameId() {
        return null;
    }

    @Override
    protected void reloadData() {

    }

    @Override
    protected int getFilterRequestCode() {
        return 0;
    }

    @Override
    protected int getSortRequestCode() {
        return 0;
    }

    @Override
    protected List<AHBottomNavigationItem> getBottomNavigationItems() {
        return null;
    }

    @Override
    protected AHBottomNavigation.OnTabSelectedListener getBottomNavClickListener() {
        return null;
    }

    @Override
    protected SearchSectionGeneralAdapter getAdapter() {
        return null;
    }

    @Override
    protected SearchSectionFragmentPresenter getPresenter() {
        return null;
    }

    @Override
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return null;
    }
}
