package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.source.ShopSettingInfoDataSource;
import com.tokopedia.seller.shop.open.domain.ShopSettingSaveInfoRepository;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingSaveInfoRepositoryImpl implements ShopSettingSaveInfoRepository {
    ShopSettingInfoDataSource shopSettingInfoDataSource;

    public ShopSettingSaveInfoRepositoryImpl(ShopSettingInfoDataSource shopSettingInfoDataSource) {
        this.shopSettingInfoDataSource = shopSettingInfoDataSource;
    }

    @Override
    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return shopSettingInfoDataSource.saveShopSetting(paramsRequest);
    }

    @Override
    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams) {
        return shopSettingInfoDataSource.saveShopSettingStep2(requestParams);
    }

    @Override
    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper) {
        return shopSettingInfoDataSource.saveShopSettingStep3(courierServiceIdWrapper);
    }

    @Override
    public Observable<Boolean> createShop() {
        return shopSettingInfoDataSource.createShop();
    }
}
