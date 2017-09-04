package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {
    Observable<AddProductShopInfoDomainModel> getAddProductShopInfo();
    String getShopId();
    Observable<Boolean> clearCacheShopInfo();
}
