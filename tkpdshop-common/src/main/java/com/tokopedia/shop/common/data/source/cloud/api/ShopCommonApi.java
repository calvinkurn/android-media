package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopCommonApi {

    @GET(ShopCommonUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfo(@Query(ShopCommonParamApiConstant.SHOP_ID) String shopId);

    @GET(ShopCommonUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(@Query(ShopCommonParamApiConstant.SHOP_DOMAIN) String shopDomain);

}
