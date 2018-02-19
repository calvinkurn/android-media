package com.tokopedia.shop.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopCommonCloudDataSource {

    private ShopCommonApi shopApi;

    public ShopCommonCloudDataSource(ShopCommonApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfo(String shopId) {
        return shopApi.getShopInfo(shopId);
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(String shopDomain) {
        return shopApi.getShopInfoByDomain(shopDomain);
    }
}
