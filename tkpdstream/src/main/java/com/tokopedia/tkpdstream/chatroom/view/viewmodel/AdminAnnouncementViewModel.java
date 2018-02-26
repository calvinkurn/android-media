package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/7/18.
 */

public class AdminAnnouncementViewModel extends BaseChatViewModel implements
        Visitable<GroupChatTypeFactory> {

    public AdminAnnouncementViewModel(String message, long createdAt, long updatedAt, String messageId) {
        super(message, createdAt, updatedAt, messageId);
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
