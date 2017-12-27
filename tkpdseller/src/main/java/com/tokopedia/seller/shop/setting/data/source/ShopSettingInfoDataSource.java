package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.core.base.domain.RequestParams;
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

    public Observable<Boolean> saveShopSetting(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1) {
        return shopSettingInfoDataSourceCloud.saveShopSetting(logo, serverId, photoObj, shopDescription, tagLine, stepInfo1);
    }

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams){
        return shopSettingInfoDataSourceCloud.saveShopSettingStep2(requestParams);
    }
}
