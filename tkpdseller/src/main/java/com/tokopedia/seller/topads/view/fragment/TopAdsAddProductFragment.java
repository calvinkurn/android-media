package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.presenter.TopAdsAddProductPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsAddProductPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsProductAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsAddProductFragmentListener;

import java.util.List;

public class TopAdsAddProductFragment extends BasePresenterFragment<TopAdsAddProductPresenter> implements TopAdsAddProductFragmentListener, SearchView.OnQueryTextListener {

    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private RecyclerView.OnScrollListener onScrollListener;
    private TopAdsProductAdapter adapter;
    private SearchView searchView;

    public static TopAdsAddProductFragment createInstance() {
        TopAdsAddProductFragment fragment = new TopAdsAddProductFragment();
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsAddProductPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_credit;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new TopAdsProductAdapter();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem) {
                    loadProduct();
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
        adapter.showLoadingFull(true);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        loadProduct();
    }

    @Override
    public void onProductListLoaded(@NonNull List<Product> productList) {
        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);
        adapter.addProductList(productList);
        if (adapter.getDataSize() <= 0) {
            adapter.showEmpty(true);
        }
    }

    @Override
    public void onLoadProductListError() {
        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);
    }

    private void loadProduct() {
        String keyword = "";
        if (searchView != null) {
            keyword = searchView.getQuery().toString();
        }
        presenter.searchProduct(keyword, adapter.getDataSize());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_ads_add_product, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.clearData();
        loadProduct();
        return true;
    }
}