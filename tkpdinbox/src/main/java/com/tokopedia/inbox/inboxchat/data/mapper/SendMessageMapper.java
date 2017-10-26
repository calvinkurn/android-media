package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageMapper implements Func1<Response<TkpdResponse>, SendMessageViewModel> {
    @Override
    public SendMessageViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
        return null;
    }
}
