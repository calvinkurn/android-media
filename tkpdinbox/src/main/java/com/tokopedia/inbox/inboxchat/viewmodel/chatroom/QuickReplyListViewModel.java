package com.tokopedia.inbox.inboxchat.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.Message;

import java.util.List;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyListViewModel implements Visitable<ChatRoomTypeFactory> {

    public List<QuickReplyViewModel> quickReplies;

    public QuickReplyListViewModel(int msgId,
                                   int fromUid,
                                   String from,
                                   String fromRole,
                                   int toUid,
                                   Message message,
                                   String startTime,
                                   String imageUri,
                                   String attachmentId,
                                   String attachmentType,
                                   FallbackAttachment fallbackAttachment,
                                   List<QuickReplyViewModel> quickReplies) {
//        super(msgId, fromUid, from, fromRole, toUid, message, startTime, imageUri,
//                attachmentId, attachmentType, fallbackAttachment);
        this.quickReplies = quickReplies;
    }

    public List<QuickReplyViewModel> getQuickReplies() {
        return quickReplies;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
