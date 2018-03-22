package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final String SPRINT_SALE = "sprint_sale";
    private ArrayList<SprintSaleProductViewModel> listProducts;
    private String redirectUrl;
    private boolean isActive;

    public SprintSaleViewModel(long createdAt, long updatedAt, String messageId,
                               String senderId, String senderName, String senderIconUrl,
                               boolean isInfluencer, boolean isAdministrator, String redirectUrl,
                               ArrayList<SprintSaleProductViewModel> listProducts, boolean isActive) {
        super("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.redirectUrl = redirectUrl;
        this.listProducts = listProducts;
        this.isActive = isActive;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public ArrayList<SprintSaleProductViewModel> getListProducts() {
        return listProducts;
    }

    public boolean isActive() {
        return isActive;
    }
}