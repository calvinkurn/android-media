package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.di.TopAdsDetailNewProductDI;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

public class TopAdsDetailNewProductFragment extends TopAdsDetailNewFragment<TopAdsDetailNewProductPresenter> {

    public static Fragment createInstance(String itemId) {
        Fragment fragment = new TopAdsDetailNewProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_ITEM_ID, itemId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewProductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_new_product;
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (detailAd !=  null) {
            presenter.saveAd((TopAdsDetailProductViewModel) detailAd, topAdsProductList);
        }
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        detailAd = new TopAdsDetailProductViewModel();
        ((TopAdsDetailProductViewModel) detailAd).setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
        ((TopAdsDetailProductViewModel) detailAd).setType(TopAdsNetworkConstant.TYPE_PRODUCT_STAT);
    }

    @Override
    protected void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, true);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, true);
        intent.putExtra(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, topAdsProductList);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }
}