package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/27/18.
 */

public class ImageViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    private String senderId;
    private String senderName;
    private String senderIconUrl;
    private boolean isInfluencer;
    private boolean isAdministrator;
    private String contentImageUrl;

    public ImageViewModel(String contentImageUrl, long createdAt, long updatedAt, String messageId,
                          String senderId, String senderName, String senderIconUrl, boolean isInfluencer,
                          boolean isAdministrator) {
        super("", createdAt, updatedAt, messageId);
        this.contentImageUrl = contentImageUrl;
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

    public String getContentImageUrl() {
        return contentImageUrl;
    }
}
