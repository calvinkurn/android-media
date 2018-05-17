package com.tokopedia.inbox.inboxchat.viewmodel.chatroom.quickreply;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.BaseChatViewModel;

import java.util.ArrayList;
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
                                   String message,
                                   String attachmentId,
                                   String attachmentType,
                                   String replyTime,
                                   List<QuickReplyViewModel> quickReplies) {
        super(msgId, fromUid, from, fromRole,
                attachmentId, attachmentType, replyTime, message);
        this.quickReplies = quickReplies;
    }

    public List<QuickReplyViewModel> getQuickReplies() {
        return quickReplies;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public QuickReplyListViewModel EMPTY() {
        return new QuickReplyListViewModel(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                new ArrayList<QuickReplyViewModel>());
    }

}
