package com.tokopedia.inbox.inboxchat.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> requestParams);

    Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters);

    Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams);
}
