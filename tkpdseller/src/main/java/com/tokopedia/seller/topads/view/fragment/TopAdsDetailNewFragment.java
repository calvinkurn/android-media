package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditPresenter;

import java.util.ArrayList;

public abstract class TopAdsDetailNewFragment<T extends TopAdsDetailEditPresenter> extends TopAdsDetailEditFragment<T> {

    protected static final int ADD_PRODUCT_REQUEST_CODE = 0;

    private View addProductLayout;
    private TextView selectedProductTextView;
    protected ArrayList<TopAdsProductViewModel> topAdsProductList;

    protected abstract void addProduct();

    @Override
    protected void initView(View view) {
        super.initView(view);
        addProductLayout = view.findViewById(R.id.layout_add_product);
        if (addProductLayout == null) {
            return;
        }
        addProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
        selectedProductTextView = (TextView) view.findViewById(R.id.text_view_selected_product);
        topAdsProductList = new ArrayList<>();
        updateSelectedProductCount();
    }

    private void updateSelectedProductCount() {
        selectedProductTextView.setText(getString(R.string.label_top_ads_total_selected_product, topAdsProductList.size()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == ADD_PRODUCT_REQUEST_CODE && intent != null) {
            topAdsProductList = intent.getParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS);
            updateSelectedProductCount();
        }
    }
}