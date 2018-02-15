package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/7/18.
 */

public class ChatViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    String senderId;
    String senderName;
    String senderIconUrl;
    boolean isInfluencer;

    public ChatViewModel(String message, String createdAt, String updatedAt,
                         String messageId, String senderId, String senderName,
                         String senderIconUrl, boolean isInfluencer) {
        super(message, createdAt, updatedAt, messageId);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
        this.isInfluencer = isInfluencer;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
