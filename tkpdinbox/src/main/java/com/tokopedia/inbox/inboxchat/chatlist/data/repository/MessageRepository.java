package com.tokopedia.inbox.inboxchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> requestParams);

    Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters);

    Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams);
}
