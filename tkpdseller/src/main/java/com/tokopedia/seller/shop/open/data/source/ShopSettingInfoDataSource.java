package com.tokopedia.seller.shop.open.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.source.cloud.ShopSettingInfoDataSourceCloud;

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

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams){
        return shopSettingInfoDataSourceCloud.saveShopSettingStep2(requestParams);
    }
    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper){
        return shopSettingInfoDataSourceCloud.saveShopSettingStep3(courierServiceIdWrapper);
    }
    public Observable<Boolean> createShop(){
        return shopSettingInfoDataSourceCloud.createShop();
    }
}
