package com.tokopedia.seller.topads.view.fragment;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditPresenter;

public abstract class TopAdsDetailNewFragment<T extends TopAdsDetailEditPresenter> extends TopAdsDetailEditFragment<T> {

    protected static final int ADD_PRODUCT_REQUEST_CODE = 0;

    private View addProductLayout;
    private TextView selectedProductTextView;

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
        updateSelectedProductCount();
    }

    private void updateSelectedProductCount() {
        selectedProductTextView.setText(getString(R.string.label_top_ads_total_selected_product, 0));
    }
}