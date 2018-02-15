package com.tokopedia.shop.product.domain.repository;

import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductRepository {

    Observable<ShopProductList> getShopProductList(ShopProductRequestModel shopProductRequestModel);
}
