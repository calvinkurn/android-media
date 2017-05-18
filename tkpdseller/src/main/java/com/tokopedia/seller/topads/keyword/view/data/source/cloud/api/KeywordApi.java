package com.tokopedia.seller.topads.keyword.view.data.source.cloud.api;

import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.view.data.model.cloud.Datum;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Test on 5/18/2017.
 */

public interface KeywordApi {

    @POST("v2/promo/keyword?keyword_id=29543")
    Observable<Response<DataResponse<String>>> getPromo(@FieldMap Map<String, String> params);

    @GET("/v1.1/dashboard/keyword")
    Observable<PageDataResponse<List<Datum>>> getDashboardKeyword(@QueryMap Map<String, String> param);

}