package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.seller.shop.setting.data.source.cache.ShopOpenDataCache;
import com.tokopedia.seller.shop.setting.data.source.cloud.ShopOpenDataCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataSource {
    private final ShopOpenDataCache shopOpenDataCache;
    private final ShopOpenDataCloud shopOpenDataCloud;

    @Inject
    public ShopOpenDataSource(ShopOpenDataCache shopOpenDataCache,
                              ShopOpenDataCloud shopOpenDataCloud) {
        this.shopOpenDataCache = shopOpenDataCache;
        this.shopOpenDataCloud = shopOpenDataCloud;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
        // stub
        // TODO look between cache and clouds
        return shopOpenDataCloud.checkDomainName(domainName);
    }

    public Observable<Boolean> checkShopName(String shopName) {
        // stub
        // TODO look between cache and cloud
        return shopOpenDataCloud.checkShopName(shopName);
    }
}
