package com.tokopedia.seller.topads.keyword.data.source.cloud.api;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Test on 5/18/2017.
 */

public interface KeywordApi {

    @GET("/v1.1/dashboard/keywords")
    Observable<PageDataResponse<List<Datum>>> getDashboardKeyword(@QueryMap Map<String, String> param);

    @FormUrlEncoded
    @POST("/v2/promo/keyword")
    Observable<PageDataResponse<List<Datum>>> postAddKeyword(@FieldMap Map<String, String> param);

}