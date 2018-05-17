package com.tokopedia.inbox.inboxchat.chattemplate.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.inbox.inboxchat.chattemplate.data.source.CloudEditTemplateChatDataSource;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatFactory {

    private EditTemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public EditTemplateChatFactory(EditTemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public CloudEditTemplateChatDataSource createCloudEditTemplateDataSource() {
        return new CloudEditTemplateChatDataSource(templateChatMapper, chatService);
    }
}
