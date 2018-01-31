package com.tokopedia.home.beranda.data.source.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.home.beranda.data.source.pojo.HomeData;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public interface HomeDataApi {
    @POST("graphql")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<HomeData>>> getHomeData(@Body String requestBody);
}