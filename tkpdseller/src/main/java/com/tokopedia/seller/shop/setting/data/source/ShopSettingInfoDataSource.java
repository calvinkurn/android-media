package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.data.source.cloud.ShopSettingInfoDataSourceCloud;

import java.util.HashMap;

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

    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return shopSettingInfoDataSourceCloud.saveShopSetting(paramsRequest);
    }
}
