package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/7/18.
 */

public class ChatViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final String ADMIN_MESSAGE = "chat";

    public ChatViewModel(String message, long createdAt, long updatedAt,
                         String messageId, String senderId, String senderName,
                         String senderIconUrl, boolean isInfluencer, boolean isAdministrator) {
        super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
