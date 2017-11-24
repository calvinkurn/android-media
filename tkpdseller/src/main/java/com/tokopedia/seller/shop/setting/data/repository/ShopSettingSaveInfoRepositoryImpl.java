package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
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
    public Observable<Boolean> saveShopSetting(String imageUrl, String shopDescription, String tagLine) {
        return shopSettingInfoDataSource.saveShopSetting();
    }

    @Override
    public Observable<GenerateHostModel.GenerateHost> generateHost() {
        return shopSettingInfoDataSource.generateHost();
    }

    @Override
    public Observable<UploadShopImageModel.Data> uploadImage(GenerateHostModel.GenerateHost generateHost, String pathFileImage) {
        return shopSettingInfoDataSource.uploadImage(generateHost, pathFileImage);
    }
}
