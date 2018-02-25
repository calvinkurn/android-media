package com.tokopedia.shop.product.view.model;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductFeaturedViewModel implements ShopProductBaseViewModel {

    private GMFeaturedProduct gmFeaturedProduct;
    private boolean whisList;

    public GMFeaturedProduct getGmFeaturedProduct() {
        return gmFeaturedProduct;
    }

    public void setGmFeaturedProduct(GMFeaturedProduct gmFeaturedProduct) {
        this.gmFeaturedProduct = gmFeaturedProduct;
    }

    public boolean isWhisList() {
        return whisList;
    }

    public void setWhisList(boolean whisList) {
        this.whisList = whisList;
    }

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
