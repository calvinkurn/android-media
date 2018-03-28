package com.tokopedia.inbox.inboxchat.data.network;

import com.tokopedia.inbox.inboxchat.data.pojo.SetChatRatingPojo;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvinatin on 27/03/18.
 */

public interface ChatRatingApi {
    @POST("/post-rating")
    @Headers({"Content-Type: application/json"})
    Observable<Response<SetChatRatingPojo>>
    setChatRating(@Body RequestParams requestParams);
}
