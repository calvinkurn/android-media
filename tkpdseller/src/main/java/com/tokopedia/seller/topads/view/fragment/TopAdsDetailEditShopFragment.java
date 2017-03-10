package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailEditShopDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditShopPresenter;

public class TopAdsDetailEditShopFragment extends TopAdsDetailEditFragment<TopAdsDetailEditShopPresenter> {

    public static Fragment createInstance(String name, String shopAdId) {
        Fragment fragment = new TopAdsDetailEditShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_edit_shop;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailEditShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        ((TopAdsDetailShopViewModel)detailAd).setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameEditText.setEnabled(false);
        promoIconView.setVisibility(View.GONE);
    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailShopViewModel) detailAd);
    }
}