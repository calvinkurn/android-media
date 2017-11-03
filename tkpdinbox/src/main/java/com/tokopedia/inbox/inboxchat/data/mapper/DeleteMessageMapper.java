package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 10/24/17.
 */

public class DeleteMessageMapper implements Func1<Response<TkpdResponse>, DeleteChatListViewModel> {
    @Override
    public DeleteChatListViewModel call(Response<TkpdResponse> response) {
        if(response.isSuccessful()){
            DeleteChatListViewModel data = response.body().convertDataObj(DeleteChatListViewModel.class);
            return data;
        }
        return null;
    }
}
