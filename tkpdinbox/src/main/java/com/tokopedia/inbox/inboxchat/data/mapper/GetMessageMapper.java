package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageMapper implements Func1<Response<TkpdResponse>, MessageData> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public MessageData call(Response<TkpdResponse> response) {
        if(response.isSuccessful()){
            MessageData data = response.body().convertDataObj(MessageData.class);
            return data;
        }else {
            return null;
        }
    }
}