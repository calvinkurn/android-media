package com.tokopedia.seller.shop.open.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.source.cloud.ShopOpenInfoDataSourceCloud;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopOpenInfoDataSource {
    private ShopOpenInfoDataSourceCloud shopOpenInfoDataSourceCloud;

    @Inject
    public ShopOpenInfoDataSource(ShopOpenInfoDataSourceCloud shopOpenInfoDataSourceCloud) {
        this.shopOpenInfoDataSourceCloud = shopOpenInfoDataSourceCloud;
    }

    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return shopOpenInfoDataSourceCloud.saveShopSetting(paramsRequest);
    }

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams){
        return shopOpenInfoDataSourceCloud.saveShopSettingStep2(requestParams);
    }
    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper){
        return shopOpenInfoDataSourceCloud.saveShopSettingStep3(courierServiceIdWrapper);
    }
    public Observable<ResponseCreateShop> createShop(){
        return shopOpenInfoDataSourceCloud.createShop();
    }

    public Observable<String> openShopPicture(String picSrc, String serverId, String url) {
        return shopOpenInfoDataSourceCloud.openShopPicture(picSrc, serverId, url);
    }
}
