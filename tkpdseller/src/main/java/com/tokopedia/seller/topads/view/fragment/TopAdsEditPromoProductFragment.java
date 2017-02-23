package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;

public class TopAdsEditPromoProductFragment extends TopAdsEditPromoFragment implements TopAdsEditPromoFragmentListener {

    private static final int ADD_PRODUCT_REQUEST_CODE = 0;

    private View addProductLayout;

    public static Fragment createInstance(String shopAdId) {
        Fragment fragment = new TopAdsEditPromoProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_product;
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

    void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }
}