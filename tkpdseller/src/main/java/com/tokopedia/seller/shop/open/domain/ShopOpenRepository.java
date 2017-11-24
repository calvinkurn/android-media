package com.tokopedia.seller.shop.open.domain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public interface ShopOpenRepository {
    Observable<Boolean> checkDomain(String domainName);
    Observable<Boolean> checkShop(String shopName);
}
