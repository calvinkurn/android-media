package com.tokopedia.inbox.inboxchat.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.MessageViewModel;

import java.util.List;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyListViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    public List<QuickReplyViewModel> quickReplies;

    public QuickReplyListViewModel(String msgId,
                                   String fromUid,
                                   String from,
                                   String fromRole,
                                   String toUid,
                                   MessageViewModel message,
                                   String attachmentId,
                                   String attachmentType,
                                   FallbackAttachmentViewModel fallbackAttachment,
                                   List<QuickReplyViewModel> quickReplies) {
        super(msgId, fromUid, from, fromRole, toUid, message,
                attachmentId, attachmentType, fallbackAttachment);
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
