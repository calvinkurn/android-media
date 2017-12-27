package com.tokopedia.seller.shop.setting.domain;


import java.util.HashMap;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopSettingSaveInfoRepository {
    Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest);
    Observable<Boolean> saveShopSettingStep2(RequestParams requestParams);
}
