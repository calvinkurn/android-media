package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.TopAdsDetailEditProductDI;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;

public class TopAdsDetailEditProductFragment extends TopAdsDetailEditFragment<TopAdsDetailEditProductPresenter> {
    public static Fragment createInstance(String name, String shopAdId) {
        Fragment fragment = new TopAdsDetailEditProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailEditProductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_edit_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameInputLayout.setHint(getString(R.string.label_top_ads_product_name));

    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (detailAd !=  null) {
            presenter.saveAd((TopAdsDetailProductViewModel) detailAd);
        }
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        // no op overridden in child
    }
}