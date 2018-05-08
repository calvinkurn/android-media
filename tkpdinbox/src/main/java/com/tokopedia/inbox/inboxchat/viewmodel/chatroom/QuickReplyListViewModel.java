package com.tokopedia.inbox.inboxchat.viewmodel.chatroom;

import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;

import java.util.List;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyListViewModel extends BaseChatViewModel {

    public QuickReplyListViewModel() {
    }

    public List<QuickReplyViewModel> quickReplies;

    public List<QuickReplyViewModel> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<QuickReplyViewModel> quickReplies) {
        this.quickReplies = quickReplies;
    }
}
