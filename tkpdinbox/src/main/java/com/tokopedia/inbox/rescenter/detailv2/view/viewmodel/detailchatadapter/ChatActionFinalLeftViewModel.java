package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;


import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatActionFinalLeftViewModel implements Visitable<DetailChatTypeFactory> {

    private ConversationDomain conversation;
    private String type;

    public ChatActionFinalLeftViewModel(ConversationDomain conversation, String type) {
        this.conversation = conversation;
        this.type = type;
    }

    public ConversationDomain getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDomain conversation) {
        this.conversation = conversation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
