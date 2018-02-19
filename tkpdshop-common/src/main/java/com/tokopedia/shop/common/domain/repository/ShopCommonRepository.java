package com.tokopedia.shop.common.domain.repository;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopCommonRepository {

    Observable<ShopInfo> getShopInfo(String shopId);

    Observable<ShopInfo> getShopInfoByDomain(String shopDomain);
}
