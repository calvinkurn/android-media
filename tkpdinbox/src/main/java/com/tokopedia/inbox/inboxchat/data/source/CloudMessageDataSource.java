package com.tokopedia.inbox.inboxchat.data.source;

import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.DeleteMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudMessageDataSource{

    private ChatService chatService;
    private GetMessageMapper getMessageMapper;
    private DeleteMessageMapper deleteMessageMapper;

    public CloudMessageDataSource(ChatService chatService, GetMessageMapper getMessageMapper, DeleteMessageMapper deleteMessageMapper) {
        this.chatService = chatService;
        this.getMessageMapper = getMessageMapper;
        this.deleteMessageMapper = deleteMessageMapper;
    }

    public Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().getMessage(requestParams).map(getMessageMapper);
    }

    public Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters) {
        return chatService.getApi().deleteMessage(parameters).map(deleteMessageMapper);
    }
}
