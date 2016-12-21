package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.presenter.TopAdsAddProductPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsAddProductPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsProductAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsAddProductFragmentListener;

import java.util.List;

import butterknife.BindView;

public class TopAdsAddProductFragment extends BasePresenterFragment<TopAdsAddProductPresenter> implements TopAdsAddProductFragmentListener {

    private static String TAG = TopAdsAddProductFragment.class.getSimpleName();

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private RecyclerView.OnScrollListener onScrollListener;
    private TopAdsProductAdapter adapter;

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
        return false;
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
    }

    @Override
    public void onLoadProductListError() {
        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);

    }

    private void loadProduct() {
        presenter.searchProduct("", adapter.getDataCount());
    }
}