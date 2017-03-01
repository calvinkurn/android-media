package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoProductDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewProductPresenter;

public class TopAdsDetailNewGroupFragment extends TopAdsDetailNewFragment<TopAdsDetailNewProductPresenter> {

    public static Fragment createInstance(String goupName, String groupId) {
        Fragment fragment = new TopAdsDetailNewGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, goupName);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsEditPromoProductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_product;
    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailProductViewModel) detailAd);
    }

    @Override
    protected void addProduct() {

    }
}