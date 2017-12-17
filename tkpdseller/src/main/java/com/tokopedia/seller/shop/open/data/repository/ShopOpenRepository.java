package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public interface ShopOpenRepository {
    Observable<Boolean> checkDomain(String domainName);
    Observable<Boolean> checkShop(String shopName);
    Observable<ResponseIsReserveDomain> isReserveDomain();
}
