package com.tokopedia.inbox.inboxchat.chatroom.data.network;

import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicePostRequest;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicesResponseWrapper;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.rating.SetChatRatingPojo;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by alvinatin on 27/03/18.
 */

public interface ChatBotApi {

    String PATH_SET_RATING = "/cs/chatbot/post-rating";
    String PATH_INVOICE_LIST = "/cs/chatbot/invoice-list";

    @POST(PATH_SET_RATING)
    @Headers({"Content-Type: application/json"})
    Observable<Response<SetChatRatingPojo>>
    setChatRating(@Body HashMap<String, Object> requestParams);


    @POST(PATH_INVOICE_LIST)
    Observable<Response<GetInvoicesResponseWrapper>>
    getTXOrderList(@QueryMap Map<String, String> params, @Body GetInvoicePostRequest body);
}
