package com.tokopedia.seller.shop.setting.domain;


import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopSettingSaveInfoRepository {
    Observable<Boolean> saveShopSetting(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1);
    Observable<Boolean> saveShopSettingStep2(RequestParams requestParams);
}
