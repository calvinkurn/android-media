package com.tokopedia.seller.shop.open.domain;


import java.util.HashMap;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopOpenSaveInfoRepository {
    Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest);
    Observable<Boolean> saveShopSettingStep2(RequestParams requestParams);
    Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper);
    Observable<ResponseCreateShop> createShop();
    Observable<String> openShopPicture(String picSrc, String serverId, String url);
}
