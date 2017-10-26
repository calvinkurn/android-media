package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationFlagDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.CustomerDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ChatRightViewModel implements Visitable<DetailChatTypeFactory> {

    private ShopDomain shop;
    private CustomerDomain customer;
    private ConversationDomain conversation;

    public ChatRightViewModel(ShopDomain shop, CustomerDomain customer, ConversationDomain conversation) {
        this.shop = shop;
        this.customer = customer;
        this.conversation = conversation;
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

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
