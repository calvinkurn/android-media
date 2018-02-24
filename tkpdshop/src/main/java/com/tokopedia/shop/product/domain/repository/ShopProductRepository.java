package com.tokopedia.shop.product.domain.repository;

import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductRepository {

    @Deprecated
    Observable<ShopProductList> getShopProductList(ShopProductRequestModel shopProductRequestModel);

    Observable<ShopProductList> getShopProductList(String baseUrl, ShopProductRequestModel shopProductRequestModel);

    Observable<DynamicFilterModel.DataValue> getShopProductFilter();
}
