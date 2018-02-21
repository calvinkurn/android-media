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
    boolean isAdministrator;


    public ChatViewModel(String message, String createdAt, String updatedAt,
                         String messageId, String senderId, String senderName,
                         String senderIconUrl, boolean isInfluencer, boolean isAdministrator) {
        super(message, createdAt, updatedAt, messageId);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
        this.isInfluencer = isInfluencer;
        this.isAdministrator = isAdministrator;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderIconUrl() {
        return senderIconUrl;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }
}
