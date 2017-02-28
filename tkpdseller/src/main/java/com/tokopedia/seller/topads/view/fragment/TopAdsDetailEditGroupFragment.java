package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoProductDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditProductPresenter;

public class TopAdsDetailEditGroupFragment extends TopAdsDetailEditFragment<TopAdsDetailEditProductPresenter> {

    public static Fragment createInstance(String shopAdId) {
        Fragment fragment = new TopAdsDetailEditGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
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
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailProductViewModel) detailAd);
    }
}