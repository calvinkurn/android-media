package com.tokopedia.shop.info.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.constant.ShopUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopApi {

    @FormUrlEncoded
    @POST(ShopUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfo(@FieldMap Map<String, String> params);
}
