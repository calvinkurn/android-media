package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;

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
}
