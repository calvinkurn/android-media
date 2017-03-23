package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.seller.shop.setting.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.setting.domain.ShopOpenRepository;

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
        // TODO stub
        return shopOpenDataSource.checkDomainName(domainName);
    }

    @Override
    public Observable<Boolean> checkShop(String shopName) {
        // TODO stub
        return shopOpenDataSource.checkShopName(shopName);
    }
}
