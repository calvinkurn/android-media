package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenRepositoryImpl implements ShopOpenRepository {
    private final ShopOpenDataSource shopOpenDataSource;

    public ShopOpenRepositoryImpl(ShopOpenDataSource shopOpenDataSource) {
        this.shopOpenDataSource = shopOpenDataSource;
    }

    @Override
    public Observable<Boolean> checkDomain(String domainName) {
        return shopOpenDataSource.checkDomainName(domainName);
    }

    @Override
    public Observable<Boolean> checkShop(String shopName) {
        return shopOpenDataSource.checkShopName(shopName);
    }

    @Override
    public Observable<ResponseIsReserveDomain> isReserveDomain() {
        return shopOpenDataSource.isReserveDomain();
    }

    @Override
    public Observable<ResponseReserveDomain> reserveShopNameDomain(String shopName, String shopDomainName) {
        return shopOpenDataSource.reserveShopNameDomain(shopName, shopDomainName);
    }
}
