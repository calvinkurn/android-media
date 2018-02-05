package com.tokopedia.inbox.inboxchat.data.source.template;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.template.EditTemplateChatMapper;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class CloudEditTemplateChatDataSource {

    private EditTemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudEditTemplateChatDataSource(EditTemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<EditTemplateViewModel> editTemplate(int index, TKPDMapParam<String, Object> parameters) {
        return chatService.getApi().editTemplate(index, parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> createTemplate(TKPDMapParam<String, Object> parameters) {
        return chatService.getApi().createTemplate(parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> deleteTemplate(int index) {
        return chatService.getApi().deleteTemplate(index, new JsonObject()).map(templateChatMapper);
    }
}
