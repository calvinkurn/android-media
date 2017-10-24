package com.tokopedia.inbox.inboxchat.data.mapper;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetReplyMapper implements Func1<Response<TkpdResponse>, ReplyData> {


    @Override
    public ReplyData call(Response<TkpdResponse> response) {
        if(response.isSuccessful()){
            ReplyData data = response.body().convertDataObj(ReplyData.class);
            return data;
        }else {
            return null;
        }
    }
}