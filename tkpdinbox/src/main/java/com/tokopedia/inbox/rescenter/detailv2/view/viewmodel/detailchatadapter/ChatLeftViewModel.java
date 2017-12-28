package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;


import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.CustomerDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatLeftViewModel implements Visitable<DetailChatTypeFactory> {

    private ShopDomain shop;
    private CustomerDomain customer;
    private ConversationDomain conversation;
    private boolean isShowTitle;

    public ChatLeftViewModel() {
    }

    public ChatLeftViewModel(ShopDomain shop, CustomerDomain customer, ConversationDomain conversation, boolean isShowTitle) {
        this.shop = shop;
        this.customer = customer;
        this.conversation = conversation;
        this.isShowTitle = isShowTitle;
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

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
