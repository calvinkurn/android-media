package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedPromoViewHolder extends AbstractViewHolder<ShopProductLimitedPromoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_promo;

    public ShopProductLimitedPromoViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
//        titleTextView = view.findViewById(R.id.title);

    }

    @Override
    public void bind(ShopProductLimitedPromoViewModel element) {
//        titleTextView.setText(element.getProductName());
//        priceTextView.setText(element.getProductPrice());
//        ImageHandler.LoadImage(productImageView, element.getProductImage700());
    }

}