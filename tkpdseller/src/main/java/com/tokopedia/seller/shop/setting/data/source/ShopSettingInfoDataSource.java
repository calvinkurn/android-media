package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.data.source.cloud.ShopSettingInfoDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingInfoDataSource {
    private ShopSettingInfoDataSourceCloud shopSettingInfoDataSourceCloud;

    @Inject
    public ShopSettingInfoDataSource(ShopSettingInfoDataSourceCloud shopSettingInfoDataSourceCloud) {
        this.shopSettingInfoDataSourceCloud = shopSettingInfoDataSourceCloud;
    }

    public Observable<Boolean> saveShopSetting() {
        return shopSettingInfoDataSourceCloud.saveShopSetting();
    }

    public Observable<GenerateHostModel.GenerateHost> generateHost() {
        return shopSettingInfoDataSourceCloud.generateHost();
    }

    public Observable<UploadShopImageModel.Data> uploadImage(GenerateHostModel.GenerateHost generateHost, String pathFileImage) {
        return shopSettingInfoDataSourceCloud.uploadImage(generateHost, pathFileImage);
    }
}
