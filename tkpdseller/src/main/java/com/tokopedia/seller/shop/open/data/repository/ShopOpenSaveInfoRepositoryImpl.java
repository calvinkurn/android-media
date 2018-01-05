package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.open.domain.ShopOpenSaveInfoRepository;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.source.ShopOpenInfoDataSource;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopOpenSaveInfoRepositoryImpl implements ShopOpenSaveInfoRepository {
    ShopOpenInfoDataSource shopOpenInfoDataSource;

    public ShopOpenSaveInfoRepositoryImpl(ShopOpenInfoDataSource shopOpenInfoDataSource) {
        this.shopOpenInfoDataSource = shopOpenInfoDataSource;
    }

    @Override
    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return shopOpenInfoDataSource.saveShopSetting(paramsRequest);
    }

    @Override
    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams) {
        return shopOpenInfoDataSource.saveShopSettingStep2(requestParams);
    }

    @Override
    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper) {
        return shopOpenInfoDataSource.saveShopSettingStep3(courierServiceIdWrapper);
    }

    @Override
    public Observable<ResponseCreateShop> createShop() {
        return shopOpenInfoDataSource.createShop();
    }

    @Override
    public Observable<String> openShopPicture(String picSrc, String serverId, String url) {
        return shopOpenInfoDataSource.openShopPicture(picSrc, serverId, url);
    }
}
