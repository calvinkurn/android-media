package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.seller.shop.setting.data.source.ShopSettingInfoDataSource;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

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
    public Observable<Boolean> saveShopSetting(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1) {
        return shopSettingInfoDataSource.saveShopSetting(logo, serverId, photoObj, shopDescription, tagLine, stepInfo1);
    }
}
