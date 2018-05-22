package com.tokopedia.discovery.imagesearch.network.apiservice.api;

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
    Observable<Response<String>> getImageSearchResults(@Body String requestBody);
}
