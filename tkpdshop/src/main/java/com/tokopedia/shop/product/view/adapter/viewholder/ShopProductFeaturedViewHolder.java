package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductFeaturedViewHolder extends ShopProductViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_featured;

    public ShopProductFeaturedViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener) {
        super(itemView, shopProductClickedListener);
    }
}
