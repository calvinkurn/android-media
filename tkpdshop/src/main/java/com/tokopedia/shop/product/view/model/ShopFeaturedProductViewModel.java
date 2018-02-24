package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;

import java.util.List;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopFeaturedProductViewModel implements Visitable<ShopProductLimitedTypeFactory> {

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

    @Override
    public int type(ShopProductLimitedTypeFactory typeFactory) {
        return 0;
    }
}
