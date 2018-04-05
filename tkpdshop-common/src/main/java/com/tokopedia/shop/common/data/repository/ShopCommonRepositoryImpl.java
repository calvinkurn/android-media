package com.tokopedia.shop.common.data.repository;

import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ShopCommonRepositoryImpl implements ShopCommonRepository {
    private final ShopCommonDataSource shopInfoDataSource;

    public ShopCommonRepositoryImpl(ShopCommonDataSource shopInfoDataSource) {
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

    @Override
    public Observable<Boolean> toggleFavouriteShop(String shopId) {
        return shopInfoDataSource.toggleFavouriteShop(shopId);
    }


}
