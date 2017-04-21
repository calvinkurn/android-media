package com.tokopedia.seller.product.data.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.domain.ShopInfoRepository;

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
    public Observable<ShopModel> getShopInfo(String userId, String deviceId, String shopId, String shopDomain) {
        return shopInfoDataSource.getShopInfo(userId, deviceId, shopId, shopDomain);
    }

    @Override
    public Observable<ShopModel> getShopInfoFromNetwork(String userId, String deviceId, String shopId, String shopDomain) {
        return shopInfoDataSource.getShopInfoFromNetwork(userId, deviceId, shopId, shopDomain);
    }
}
