package com.tokopedia.shop.info.domain.repository;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;

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
    public Observable<Boolean> saveShopId(String shopId) {
        return shopInfoDataSource.saveShopId(shopId);
    }

    @Override
    public Observable<String> getShopId() {
        return shopInfoDataSource.getShopId();
    }

    @Override
    public Observable<ShopInfo> getShopInfo() {
        return shopInfoDataSource.getShopInfo();
    }

}
