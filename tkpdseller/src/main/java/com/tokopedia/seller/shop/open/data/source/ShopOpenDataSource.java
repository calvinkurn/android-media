package com.tokopedia.seller.shop.open.data.source;

import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;
import com.tokopedia.seller.shop.open.data.source.cloud.ShopOpenDataCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataSource {
    private final ShopOpenDataCloud shopOpenDataCloud;

    @Inject
    public ShopOpenDataSource(ShopOpenDataCloud shopOpenDataCloud) {
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