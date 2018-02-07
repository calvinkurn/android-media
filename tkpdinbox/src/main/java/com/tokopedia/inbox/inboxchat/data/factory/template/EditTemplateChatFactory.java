package com.tokopedia.inbox.inboxchat.data.factory.template;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.mapper.template.EditTemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.source.template.CloudEditTemplateChatDataSource;

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
