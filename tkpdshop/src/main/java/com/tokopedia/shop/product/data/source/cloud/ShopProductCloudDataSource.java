package com.tokopedia.shop.product.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
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

    public Observable<Response<DataResponse<PagingList<ShopProduct>>>> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        String baseUrl = ShopUrl.BASE_ACE_URL;
        if (shopProductRequestModel.isShopClosed()) {
            baseUrl = ShopCommonUrl.BASE_URL;
        }
        return shopApi.getShopProductList(baseUrl + "/" + ShopUrl.SHOP_PRODUCT_PATH, shopProductRequestModel.getHashMap());
    }
}
