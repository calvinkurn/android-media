package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/27/18.
 */

public class ImageViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    private String contentImageUrl;

    public ImageViewModel(String contentImageUrl, long createdAt, long updatedAt, String messageId,
                          String senderId, String senderName, String senderIconUrl, boolean isInfluencer,
                          boolean isAdministrator) {
        super("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.contentImageUrl = contentImageUrl;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }
}
