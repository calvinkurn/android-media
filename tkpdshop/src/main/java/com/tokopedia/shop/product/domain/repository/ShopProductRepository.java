package com.tokopedia.shop.product.domain.repository;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductRepository {

    Observable<PagingList<ShopProduct>> getShopProductList(ShopProductRequestModel shopProductRequestModel);

    Observable<List<ShopProductCampaign>> getProductCampaigns(String ids);
}
