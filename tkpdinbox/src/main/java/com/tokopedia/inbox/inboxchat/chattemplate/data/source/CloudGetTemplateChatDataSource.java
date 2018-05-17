package com.tokopedia.inbox.inboxchat.data.source.template;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudGetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudGetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<GetTemplateViewModel> getTemplate(TKPDMapParam<String, Object> parameters) {
        return chatService.getApi().getTemplate(parameters).map(templateChatMapper);
    }
}
