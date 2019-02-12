package com.tokopedia.discovery.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.OnScrollListenerAutocomplete;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteCallback;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchAdapter;
import com.tokopedia.discovery.search.view.adapter.factory.SearchAdapterTypeFactory;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchResultFragment extends TkpdBaseV4Fragment {

    private static final String TAG = SearchResultFragment.class.getSimpleName();
    private static final String ARGS_INSTANCE_NAME = "ARGS_INSTANCE_NAME";
    private static final String DEFAULT_INSTANCE_TPE = "unknown";
    private static final String ARGS_INSTANCE_TYPE = "ARGS_INSTANCE_TYPE";
    private SearchAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ItemClickListener clickListener;
    private TabAutoCompleteCallback tabAutoCompleteListener;
    private String instanceType;
    private int instanceIndex;

    private RecyclerView recyclerView;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(String tabName,
                                                   int tabIndex,
                                                   ItemClickListener clickListener,
                                                   TabAutoCompleteCallback tabAutoCompleteListener) {

        Bundle args = new Bundle();
        args.putString(ARGS_INSTANCE_NAME, tabName);
        args.putInt(ARGS_INSTANCE_TYPE, tabIndex);

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setCallBackListener(clickListener);
        fragment.setHostListener(tabAutoCompleteListener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setHostListener(TabAutoCompleteCallback tabAutoCompleteListener) {
        this.tabAutoCompleteListener = tabAutoCompleteListener;
    }

    private void setCallBackListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        instanceType = getArguments().getString(ARGS_INSTANCE_NAME, DEFAULT_INSTANCE_TPE);
        instanceIndex = getArguments().getInt(ARGS_INSTANCE_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_search_result, container, false);
        initView(parentView);
        prepareView(parentView);
        return parentView;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (tabAutoCompleteListener != null) {
            tabAutoCompleteListener.onAdapterReady(instanceIndex, adapter);
        }
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(instanceType, clickListener);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new SearchAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new OnScrollListenerAutocomplete(view.getContext(), view));
    }

    public void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

}