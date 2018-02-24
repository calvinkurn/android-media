package com.tokopedia.shop.product.view.model;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopPageFeaturedProduct {

    private GMFeaturedProduct gmFeaturedProduct;
    private boolean whislist;

    public GMFeaturedProduct getGmFeaturedProduct() {
        return gmFeaturedProduct;
    }

    public void setGmFeaturedProduct(GMFeaturedProduct gmFeaturedProduct) {
        this.gmFeaturedProduct = gmFeaturedProduct;
    }

    public boolean isWhislist() {
        return whislist;
    }

    public void setWhislist(boolean whislist) {
        this.whislist = whislist;
    }
}
