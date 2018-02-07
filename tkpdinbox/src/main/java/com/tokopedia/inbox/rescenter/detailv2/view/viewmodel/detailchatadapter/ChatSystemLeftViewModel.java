package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;


import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.CustomerDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatSystemLeftViewModel implements Visitable<DetailChatTypeFactory> {

    private ShopDomain shop;
    private CustomerDomain customer;
    private ConversationDomain conversation;
    private String actionType;

    public ChatSystemLeftViewModel() {
    }

    public ChatSystemLeftViewModel(ShopDomain shop, CustomerDomain customer, ConversationDomain conversation, String actionType) {
        this.shop = shop;
        this.customer = customer;
        this.conversation = conversation;
        this.actionType = actionType;
    }

    public ShopDomain getShop() {
        return shop;
    }

    public void setShop(ShopDomain shop) {
        this.shop = shop;
    }

    public CustomerDomain getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDomain customer) {
        this.customer = customer;
    }

    public ConversationDomain getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDomain conversation) {
        this.conversation = conversation;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
