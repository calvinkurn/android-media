package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 10/24/17.
 */

public class DeleteMessageMapper implements Func1<Response<TkpdResponse>, InboxChatViewModel> {
    @Override
    public InboxChatViewModel call(Response<TkpdResponse> response) {
        return null;
    }
}
