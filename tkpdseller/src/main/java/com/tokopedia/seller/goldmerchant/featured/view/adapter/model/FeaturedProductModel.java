package com.tokopedia.seller.goldmerchant.featured.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductModel implements ItemType {
    public static int TYPE = 128391;
    private long productId;

    public FeaturedProductModel(long productId) {
        this.productId = productId;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public long getProductId() {
        return productId;
    }
}
