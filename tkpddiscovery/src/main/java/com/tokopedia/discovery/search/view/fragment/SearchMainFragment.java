package com.tokopedia.discovery.search.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteAdapter;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteFactory;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteTypeFactory;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchPageAdapter;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchMainFragment extends TkpdBaseV4Fragment implements SearchContract.View, ItemClickListener {
    public static final int PAGER_POSITION_PRODUCT = 0;
    public static final int PAGER_POSITION_SHOP = 1;


    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";
    private static final String SEARCH_INIT_KEY = "SEARCH_INIT_KEY";

    RecyclerView recyclerView;
//    TabLayout tabLayout;
//    ViewPager viewPager;

    SearchPresenter presenter;
    private HostAutoCompleteAdapter adapter;
//    private SearchPageAdapter pageAdapter;
    private String mSearch = "";
    private String networkErrorMessage;

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
        initResources();
        presenter = new SearchPresenter(getActivity());
        setRetainInstance(true);
    }

    private void initResources() {
        networkErrorMessage = getString(R.string.msg_network_error);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_universearch, container, false);
        recyclerView = parentView.findViewById(R.id.list);
        prepareView();
        presenter.attachView(this);
        presenter.initializeDataSearch();
        return parentView;
    }

    private void prepareView() {
        HostAutoCompleteTypeFactory typeFactory = new HostAutoCompleteFactory(
                this,
                getChildFragmentManager()
        );
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        adapter = new HostAutoCompleteAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

//        pageAdapter = new SearchPageAdapter(getChildFragmentManager(), getActivity());
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(pageAdapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

    public void setCurrentTab(final int pos) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                if (viewPager != null) {
//                    viewPager.setCurrentItem(pos);
//                }
            }
        });
    }

    public int getCurrentTab() {
        return 0;
//        return viewPager.getCurrentItem();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void showAutoCompleteResult(DefaultAutoCompleteViewModel defaultAutoCompleteViewModel,
                                       TabAutoCompleteViewModel tabAutoCompleteViewModel) {
        adapter.setDefaultViewModel(defaultAutoCompleteViewModel);
        adapter.setSuggestionViewModel(tabAutoCompleteViewModel);
        if (defaultAutoCompleteViewModel.getList().isEmpty()) {
            recyclerView.scrollToPosition(1);
        } else {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void showSearchResult(final List<Visitable> list) {
//        SearchResultFragment resultFragment = pageAdapter.getRegisteredFragment(0);
//        resultFragment.clearData();
//        SearchResultFragment shopFragment = pageAdapter.getRegisteredFragment(1);
//        shopFragment.clearData();
//        for (Visitable visitable : list) {
//            if(visitable instanceof ShopViewModel){
//                shopFragment.addSearchResult(visitable);
//            } else {
//                resultFragment.addSearchResult(visitable);
//            }
//        }
    }

    @Override
    public void showNetworkErrorMessage() {
        throw new RuntimeException("showNetworkErrorMessage()");
//        SnackbarManager.make(getActivity(), networkErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null) {
            mSearch = savedInstanceState.getString(SEARCH_INIT_KEY);
            presenter.search(mSearch);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_INIT_KEY, mSearch);
    }

    public void search(String query){
        this.mSearch = query;
        presenter.search(mSearch);
    }

    public void deleteAllRecentSearch(){
        presenter.deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword){
        presenter.deleteRecentSearchItem(keyword);
    }

    @Override
    public void onItemClicked(String applink, String webUrl) {

    }

    @Override
    public void onItemClicked(SearchItem item) {
        probeAnalytics(item);
        ((DiscoveryActivity) getActivity()).dropKeyboard();
        if (item.getEventAction().equals("shop") && item.getApplink() != null) {
            List<String> segments = Uri.parse(item.getApplink()).getPathSegments();
            if (segments != null && segments.size() > 0) {
                Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), segments.get(0));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        } else if (item.getSc() != null && !item.getSc().isEmpty()) {
            ((DiscoveryActivity) getActivity()).onSuggestionProductClick(item.getKeyword(), item.getSc());
        } else {
            ((DiscoveryActivity) getActivity()).onSuggestionProductClick(item.getKeyword());
        }
    }


    private void probeAnalytics(SearchItem item){
        switch (item.getEventAction())
        {
            case AppEventTracking.GTM.SEARCH_AUTOCOMPLETE :
                UnifyTracking.eventClickAutoCompleteSearch(item.getKeyword());
                break;
            case AppEventTracking.GTM.SEARCH_HOTLIST :
                UnifyTracking.eventClickHotListSearch(item.getKeyword());
                break;
            case AppEventTracking.GTM.SEARCH_RECENT :
                UnifyTracking.eventClickRecentSearch(item.getKeyword());
                break;
            case AppEventTracking.GTM.SEARCH_POPULAR :
                UnifyTracking.eventClickPopularSearch(item.getKeyword());
                break;
            case AppEventTracking.GTM.SEARCH_AUTOCOMPLETE_IN_CAT :
                UnifyTracking.eventClickAutoCompleteCategory(item.getRecom(), item.getSc(), item.getKeyword());
                break;
        }
    }

    @Override
    public void copyTextToSearchView(String text) {
        ((DiscoveryActivity) getActivity()).setSearchQuery(text + " ");
    }

    @Override
    public void onDeleteRecentSearchItem(SearchItem item) {
        ((DiscoveryActivity) getActivity()).deleteRecentSearch(item.getKeyword());
    }

    @Override
    public void onDeleteAllRecentSearch() {
        ((DiscoveryActivity) getActivity()).deleteAllRecentSearch();
    }
}
