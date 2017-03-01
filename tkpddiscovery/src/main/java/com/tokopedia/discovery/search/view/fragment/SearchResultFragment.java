package com.tokopedia.discovery.search.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchAdapter;
import com.tokopedia.discovery.search.view.adapter.factory.SearchAdapterTypeFactory;

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
//        ((AppCompatActivity) getActivity()).getSupportActionBar()
//                .setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),
//                        R.color.tkpd_green_header)));
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

    public void addSearchResult(Visitable visitable){
        adapter.addList(visitable);
    }

    private void prepareView(View parentView) {
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new SearchAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void clearData() {
        adapter.clearData();
    }

    @Override
    public void onItemClicked(SearchItem item) {
        ((BrowseProductActivity) getActivity()).sendQuery(item.getKeyword());
    }

    @Override
    public void copyTextToSearchView(String text) {
        ((BrowseProductActivity) getActivity()).setSearchQuery(text);
    }

    @Override
    public void onDeleteRecentSearchItem(SearchItem item) {
        ((BrowseProductActivity) getActivity()).deleteRecentSearch(item.getKeyword());
    }

    @Override
    public void onDeleteAllRecentSearch() {
        ((BrowseProductActivity) getActivity()).deleteAllRecentSearch();
    }
}