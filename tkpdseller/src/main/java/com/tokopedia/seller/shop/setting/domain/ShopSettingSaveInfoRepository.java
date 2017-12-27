package com.tokopedia.seller.shop.setting.domain;


import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopSettingSaveInfoRepository {

    Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest);
}
