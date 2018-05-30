package com.tokopedia.discovery.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
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

public class SearchResultFragment extends TkpdBaseV4Fragment {

    private static final String TAG = SearchResultFragment.class.getSimpleName();
    private Unbinder unbinder;
    private SearchAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ItemClickListener clickListener;

    @BindView(R2.id.list)
    RecyclerView recyclerView;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(ItemClickListener clickListener) {

        Bundle args = new Bundle();

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setCallBackListener(clickListener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setCallBackListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
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

    public void addBulkSearchResult(List<Visitable> list) {
        if (adapter != null) {
            adapter.addAll(list);
        }
    }

    private void prepareView(View view) {
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(clickListener);
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

}