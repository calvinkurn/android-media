package com.tokopedia.discovery.imagesearch.network.apiservice.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.network.entity.discovery.ImageSearchProductResponse;
import com.tokopedia.core.network.entity.discovery.SearchProductResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sachinbansal on 5/21/18.
 */

public interface ImageSearchApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<GraphqlResponse<ImageSearchProductResponse>> getImageSearchResults(@Body Map<String, Object> requestBody);

}
