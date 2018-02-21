package com.tokopedia.profile.data.network;

import com.tokopedia.profile.data.pojo.GraphqlResponse;
import com.tokopedia.profile.data.pojo.ProfileGraphql;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by alvinatin on 19/02/18.
 */

public interface ProfileApi {
    @POST("graphql")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<ProfileGraphql>>> getProfile(@Body String requestBody);
}
