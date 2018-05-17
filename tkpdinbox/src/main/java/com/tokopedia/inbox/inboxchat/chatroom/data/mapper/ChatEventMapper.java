package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.ChatSocketData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ChatEventMapper implements Func1<Response<TkpdResponse>, ChatSocketData> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public ChatSocketData call(Response<TkpdResponse> response) {
        if(response.isSuccessful()){
            ChatSocketData data = response.body().convertDataObj(ChatSocketData.class);
            return data;
        }else {
            return null;
        }
    }
}