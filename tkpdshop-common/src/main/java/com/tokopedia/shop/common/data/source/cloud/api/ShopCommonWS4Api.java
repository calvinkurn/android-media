package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.source.cloud.model.ShopFavourite;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopCommonWS4Api {

    @POST(ShopCommonUrl.TOGGLE_FAVOURITE_SHOP)
    @FormUrlEncoded
    Observable<Response<DataResponse<ShopFavourite>>> toggleFavouriteShop(@Field(ShopCommonParamApiConstant.SHOP_ID) String shopId,
                                                                          @Field(ShopCommonParamApiConstant.USER_ID) String userId,
                                                                          @Field(ShopCommonParamApiConstant.OS_TYPE) String osType,
                                                                          @Field(ShopCommonParamApiConstant.DEVICE_ID) String deviceId);
}
