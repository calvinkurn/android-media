package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 3/29/18.
 */

public class GeneratedMessageViewModel extends BaseChatViewModel implements
        Visitable<GroupChatTypeFactory> {

    public static final String TYPE = "generated_msg";

    public GeneratedMessageViewModel(String message, long createdAt, long updatedAt,
                                     String messageId, String senderId, String senderName,
                                     String senderIconUrl, boolean isInfluencer, boolean isAdministrator) {
        super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl, isInfluencer, isAdministrator);
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
