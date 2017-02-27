package com.tokopedia.seller.shopscore.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.di.ShopScoreDetailDependencyInjector;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailViewModel;
import com.tokopedia.seller.shopscore.view.presenter.ShopScoreDetailPresenterImpl;
import com.tokopedia.seller.shopscore.view.recyclerview.ShopScoreDetailAdapter;

import java.util.List;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailFragment extends BaseDaggerFragment implements ShopScoreDetailView {
    public static final String TAG = "ShopScoreDetail";
    private RecyclerView recyclerView;
    private ShopScoreDetailAdapter adapter;
    private ShopScoreDetailPresenterImpl presenter;

    public static Fragment createFragment() {
        return new ShopScoreDetailFragment();
    }

    @Override
    protected void initInjector() {
        presenter = ShopScoreDetailDependencyInjector.getPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_shop_score_detail, container, false);
        setupRecyclerView(parentView);
        presenter.attachView(this);
        presenter.getShopScoreDetail();
        return parentView;
    }

    private void setupRecyclerView(View parentView) {
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view_shop_score_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopScoreDetailAdapter();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void renderShopScoreDetail(List<ShopScoreDetailViewModel> viewModel) {
        adapter.updateData(viewModel);
    }
}
