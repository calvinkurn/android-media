package com.tokopedia.discovery.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R2;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.discovery.search.view.adapter.SearchPageAdapter;
import com.tokopedia.discovery.search.view.adapter.viewmodel.ShopViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author erry on 23/02/17.
 */

public class SearchMainFragment extends TkpdBaseV4Fragment implements SearchContract.View {

    private Unbinder unbinder;
    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";

    @BindView(R2.id.tabs)
    TabLayout tabLayout;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;

    SearchPresenter presenter;
    private SearchPageAdapter pageAdapter;

    public static SearchMainFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchMainFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString(INIT_QUERY, query);
        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_universearch, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView();
        presenter.attachView(this);
        presenter.initializeDataSearch();
        return parentView;
    }

    private void prepareView(){
        pageAdapter = new SearchPageAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void showSearchResult(final List<Visitable> list) {
        SearchResultFragment resultFragment = (SearchResultFragment) pageAdapter.getItem(0);
        resultFragment.clearData();
        SearchResultFragment shopFragment = (SearchResultFragment) pageAdapter.getItem(1);
        shopFragment.clearData();
        for (Visitable visitable : list) {
            if(visitable instanceof ShopViewModel){
                shopFragment.addSearchResult(visitable);
            } else {
                resultFragment.addSearchResult(visitable);
            }
        }
    }

    public void search(String query){
        presenter.search(query);
    }

    public void deleteAllRecentSearch(){
        presenter.deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword){
        presenter.deleteRecentSearchItem(keyword);
    }
}
