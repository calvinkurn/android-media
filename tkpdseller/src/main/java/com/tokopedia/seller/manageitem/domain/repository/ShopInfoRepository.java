package com.tokopedia.seller.manageitem.domain.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.manageitem.data.model.AddProductShopInfoDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {
    Observable<AddProductShopInfoDomainModel> getAddProductShopInfo();
    String getShopId();
    Observable<ShopModel> getShopInfo();
}
