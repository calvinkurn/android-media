package com.tokopedia.seller.shop.setting.domain;


import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopSettingSaveInfoRepository {
    Observable<Boolean> saveShopSetting(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1);
}
