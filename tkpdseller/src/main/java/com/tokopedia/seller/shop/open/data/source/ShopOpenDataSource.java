package com.tokopedia.seller.shop.open.data.source;

import com.tokopedia.seller.shop.open.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;
import com.tokopedia.seller.shop.setting.data.source.cache.ShopOpenDataCache;
import com.tokopedia.seller.shop.open.data.source.cloud.ShopOpenDataCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataSource {
    private final ShopOpenDataCache shopOpenDataCache;
    private final ShopOpenDataCloud shopOpenDataCloud;

    @Inject
    public ShopOpenDataSource(ShopOpenDataCache shopOpenDataCache, ShopOpenDataCloud shopOpenDataCloud) {
        this.shopOpenDataCache = shopOpenDataCache;
        this.shopOpenDataCloud = shopOpenDataCloud;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
        return shopOpenDataCloud.checkDomainName(domainName);
    }

    public Observable<Boolean> checkShopName(String shopName) {
        return shopOpenDataCloud.checkShopName(shopName);
    }

    public Observable<ResponseIsReserveDomain> isReserveDomain() {
        return shopOpenDataCloud.isReserveDomainResponseObservable();
    }

    public Observable<ResponseReserveDomain> reserveShopNameDomain(String shopName, String shopDomainName) {
        return shopOpenDataCloud.reserveShopNameDomain(shopName, shopDomainName);
    }
}