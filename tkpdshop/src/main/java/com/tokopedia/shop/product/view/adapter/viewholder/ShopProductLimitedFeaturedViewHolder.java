package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedFeaturedViewHolder extends AbstractViewHolder<ShopProductLimitedFeaturedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_product_grid;

    public ShopProductLimitedFeaturedViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
//        titleTextView = view.findViewById(R.id.title);

    }

    @Override
    public void bind(ShopProductLimitedFeaturedViewModel element) {
//        titleTextView.setText(element.getProductName());
//        priceTextView.setText(element.getProductPrice());
//        ImageHandler.LoadImage(productImageView, element.getProductImage700());
    }

}