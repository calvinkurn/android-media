package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailEditProductDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditProductPresenter;

public class TopAdsDetailEditProductFragment extends TopAdsDetailEditFragment<TopAdsDetailEditProductPresenter> {

    TextView infoPriceLimit;

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
        infoPriceLimit = (TextView) view.findViewById(R.id.info_limit_price);
        infoPriceLimit.setVisibility(View.GONE);
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
        presenter.saveAd((TopAdsDetailProductViewModel) detailAd);
    }
}