package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ChatNotSupportedRightViewModel implements Visitable<DetailChatTypeFactory> {

    private ConversationDomain conversation;

    public ChatNotSupportedRightViewModel(ConversationDomain conversation) {
        this.conversation = conversation;
    }

    public ConversationDomain getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDomain conversation) {
        this.conversation = conversation;
    }

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
