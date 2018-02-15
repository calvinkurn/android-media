package com.tokopedia.shop.product.data.source.cloud;

import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductCloudDataSource {

    private ShopApi shopApi;

    @Inject
    public ShopProductCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }
}
