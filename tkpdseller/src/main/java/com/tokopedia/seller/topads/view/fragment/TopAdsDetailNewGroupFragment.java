package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoShopDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditShopPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewShopPresenter;

public class TopAdsDetailNewGroupFragment extends TopAdsDetailNewFragment<TopAdsDetailNewShopPresenter> {

    private EditText shopNameEditText;
    private String shopName;

    public static Fragment createInstance(String goupName, String groupId) {
        Fragment fragment = new TopAdsDetailNewGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, goupName);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        shopName = bundle.getString(TopAdsExtraConstant.EXTRA_SHOP_NAME);
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsEditPromoShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        shopNameEditText = (EditText) view.findViewById(R.id.edit_text_shop_name);
        shopNameEditText.setText(shopName);
    }

    @Override
    protected void loadAd(TopAdsDetailAdViewModel detailAd) {
        super.loadAd(detailAd);
        shopNameEditText.setText(detailAd.getTitle());
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailShopViewModel) detailAd);
    }
}