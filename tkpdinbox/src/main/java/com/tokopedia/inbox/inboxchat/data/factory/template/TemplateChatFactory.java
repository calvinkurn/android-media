package com.tokopedia.inbox.inboxchat.data.factory.template;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.source.template.CloudGetTemplateChatDataSource;
import com.tokopedia.inbox.inboxchat.data.source.template.CloudSetTemplateChatDataSource;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateChatFactory {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public TemplateChatFactory(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public CloudGetTemplateChatDataSource createCloudGetTemplateDataSource() {
        return new CloudGetTemplateChatDataSource(templateChatMapper, chatService);
    }

    public CloudSetTemplateChatDataSource createCloudSetTemplateDataSource() {
        return new CloudSetTemplateChatDataSource(templateChatMapper, chatService);
    }
}
