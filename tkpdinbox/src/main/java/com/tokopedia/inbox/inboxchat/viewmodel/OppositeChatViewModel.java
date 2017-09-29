package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class OppositeChatViewModel extends ListReplyViewModel{

    public OppositeChatViewModel(int replyId, String senderId, String msg, String replyTime, int fraudStatus, String readTime, int attachmentId, int oldMsgId) {
        super(replyId, senderId, msg, replyTime, fraudStatus, readTime, attachmentId, oldMsgId);
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
