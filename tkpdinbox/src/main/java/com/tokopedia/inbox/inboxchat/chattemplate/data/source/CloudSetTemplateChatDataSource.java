package com.tokopedia.inbox.inboxchat.data.source.template;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudSetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudSetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<GetTemplateViewModel> setTemplate(JsonObject parameters) {
        return chatService.getApi().setTemplate(parameters).map(templateChatMapper);
    }
}
