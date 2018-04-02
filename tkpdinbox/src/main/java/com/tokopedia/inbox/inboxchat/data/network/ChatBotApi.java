package com.tokopedia.inbox.inboxchat.data.network;

import com.tokopedia.inbox.inboxchat.data.pojo.SetChatRatingPojo;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvinatin on 27/03/18.
 */

public interface ChatBotApi {

    String PATH_SET_RATING = "/cs/chatbot/post-rating";

    @POST(PATH_SET_RATING)
    @Headers({"Content-Type: application/json"})
    Observable<Response<SetChatRatingPojo>>
    setChatRating(@Body HashMap<String, Object> requestParams);
}
