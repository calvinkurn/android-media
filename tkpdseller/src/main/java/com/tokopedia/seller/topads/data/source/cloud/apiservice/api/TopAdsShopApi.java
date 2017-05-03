package com.tokopedia.seller.topads.data.source.cloud.apiservice.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.topads.data.model.data.DataEtalase;
import com.tokopedia.seller.topads.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
public interface TopAdsShopApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<DataResponse<DataEtalase>>> getShopEtalase(@FieldMap Map<String, String> params);
}