package com.tokopedia.shop.info.data.repository;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    private final ShopInfoDataSource shopInfoDataSource;

    @Inject
    public ShopInfoRepositoryImpl(ShopInfoDataSource shopInfoDataSource) {
        this.shopInfoDataSource = shopInfoDataSource;
    }

    @Override
    public Observable<ShopInfo> getShopInfo(String shopId) {
        return shopInfoDataSource.getShopInfo(shopId);
    }

    @Override
    public Observable<ShopInfo> getShopInfoByDomain(String shopDomain) {
        return shopInfoDataSource.getShopInfoByDomain(shopDomain);
    }
}
