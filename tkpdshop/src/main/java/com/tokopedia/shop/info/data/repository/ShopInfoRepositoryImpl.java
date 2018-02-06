package com.tokopedia.shop.info.data.repository;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;

import java.util.List;

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
    public Observable<List<ShopNote>> getShopNoteList(String shopId) {
        return shopInfoDataSource.getShopNoteList(shopId);
    }

}
