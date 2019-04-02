package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatCreateLeftViewModel implements Visitable<DetailChatTypeFactory> {

    private ShopDomain shopDomain;
    private LastDomain lastDomain;
    private ConversationDomain conversationDomain;
    private String actionType;

    public ChatCreateLeftViewModel(ShopDomain shopDomain, LastDomain lastDomain, ConversationDomain conversationDomain, String actionType) {
        this.shopDomain = shopDomain;
        this.lastDomain = lastDomain;
        this.conversationDomain = conversationDomain;
        this.actionType = actionType;
    }

    public ShopDomain getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(ShopDomain shopDomain) {
        this.shopDomain = shopDomain;
    }

    public LastDomain getLastDomain() {
        return lastDomain;
    }

    public void setLastDomain(LastDomain lastDomain) {
        this.lastDomain = lastDomain;
    }

    public ConversationDomain getConversationDomain() {
        return conversationDomain;
    }

    public void setConversationDomain(ConversationDomain conversationDomain) {
        this.conversationDomain = conversationDomain;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public int type(DetailChatTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
