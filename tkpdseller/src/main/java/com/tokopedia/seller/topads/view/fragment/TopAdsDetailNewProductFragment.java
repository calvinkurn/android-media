package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoProductDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditProductPresenter;

public class TopAdsDetailNewProductFragment extends TopAdsDetailNewFragment<TopAdsDetailEditProductPresenter> {

    private static final int ADD_PRODUCT_REQUEST_CODE = 0;

    private View addProductLayout;

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsDetailNewProductFragment();
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
    protected void loadAdDetail() {

    }

    void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }
}