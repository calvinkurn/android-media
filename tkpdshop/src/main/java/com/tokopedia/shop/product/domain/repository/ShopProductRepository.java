package com.tokopedia.shop.product.domain.repository;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.product.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductRepository {

    Observable<PagingList<ShopProduct>> getShopProductList(ShopProductRequestModel shopProductRequestModel);

    Observable<DynamicFilterModel.DataValue> getShopProductFilter();

    Observable<PagingListOther<EtalaseModel>> getShopEtalaseList(ShopEtalaseRequestModel shopProductRequestModel);
}
