package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoProductDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoProductPresenter;

public class TopAdsEditPromoNewProductFragment extends TopAdsEditPromoFragment<TopAdsEditPromoProductPresenter> {

    private static final int ADD_PRODUCT_REQUEST_CODE = 0;

    private View addProductLayout;

    public static Fragment createInstance(String shopAdId) {
        Fragment fragment = new TopAdsEditPromoNewProductFragment();
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
        return R.layout.fragment_top_ads_edit_new_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        addProductLayout = view.findViewById(R.id.layout_add_product);
        addProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        presenter.getDetailAd(adId);
    }

    void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }
}