package com.tokopedia.discovery.search.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchAdapter;
import com.tokopedia.discovery.search.view.adapter.factory.SearchAdapterTypeFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author erry on 23/02/17.
 */

public class SearchResultFragment extends TkpdBaseV4Fragment
        implements ItemClickListener {

    private static final String TAG = SearchResultFragment.class.getSimpleName();
    private Unbinder unbinder;
    private SearchAdapter adapter;
    private LinearLayoutManager layoutManager;

    @BindView(R2.id.list)
    RecyclerView recyclerView;

    public static SearchResultFragment newInstance() {

        Bundle args = new Bundle();

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_search_result, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView(parentView);
        return parentView;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addSearchResult(Visitable visitable) {
        if (adapter != null) {
            adapter.addList(visitable);
        }
    }

    private void prepareView(View view) {
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new SearchAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
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