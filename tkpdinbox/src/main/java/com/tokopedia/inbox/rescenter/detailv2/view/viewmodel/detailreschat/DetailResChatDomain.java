package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class DetailResChatDomain {

    private NextActionDomain nextAction;
    private ShopDomain shop;
    private CustomerDomain customer;
    private ResolutionDomain resolution;
    private ButtonDomain button;
    private int actionBy;
    private List<ConversationDomain> conversation;

    private boolean success;

    public DetailResChatDomain(NextActionDomain nextAction,
                               ShopDomain shop,
                               CustomerDomain customer,
                               ResolutionDomain resolution,
                               ButtonDomain button,
                               int actionBy,
                               List<ConversationDomain> conversation) {
        this.nextAction = nextAction;
        this.shop = shop;
        this.customer = customer;
        this.resolution = resolution;
        this.button = button;
        this.actionBy = actionBy;
        this.conversation = conversation;
    }

    public NextActionDomain getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionDomain nextAction) {
        this.nextAction = nextAction;
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

    public ResolutionDomain getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionDomain resolution) {
        this.resolution = resolution;
    }

    public ButtonDomain getButton() {
        return button;
    }

    public void setButton(ButtonDomain button) {
        this.button = button;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public List<ConversationDomain> getConversation() {
        return conversation;
    }

    public void setConversation(List<ConversationDomain> conversation) {
        this.conversation = conversation;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

