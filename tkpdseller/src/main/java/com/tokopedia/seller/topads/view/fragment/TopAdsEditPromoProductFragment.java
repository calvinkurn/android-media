package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;

public class TopAdsEditPromoProductFragment extends TopAdsEditPromoFragment implements TopAdsEditPromoFragmentListener {

    RecyclerView productListRecylerView;
    Button addProductButton;

    public static TopAdsEditPromoProductFragment createInstance() {
        TopAdsEditPromoProductFragment fragment = new TopAdsEditPromoProductFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop;
    }

    @Override
    protected void initView(View view) {
        productListRecylerView = (RecyclerView) view.findViewById(R.id.recyler_view_product_list);
        addProductButton = (Button) view.findViewById(R.id.button_add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }

    void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductActivity.class);
        startActivity(intent);
    }

}