package com.tokopedia.shop.product.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductCloudDataSource {

    private ShopApi shopApi;

    @Inject
    public ShopProductCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopProductList>>> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        return shopApi.getShopProductList(shopProductRequestModel.getHashMap());
    }
}
