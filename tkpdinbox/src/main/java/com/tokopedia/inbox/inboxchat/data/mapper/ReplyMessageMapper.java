package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;

import org.json.JSONException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.as
 */

public class ReplyMessageMapper implements Func1<Response<TkpdResponse>, ReplyActionData> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public ReplyActionData call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            ReplyActionData data = response.body().convertDataObj(ReplyActionData.class);
            return data;
        } else {
            return new ReplyActionData();
        }
    }
}