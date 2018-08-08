package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class DetailResChatResponse {

    @SerializedName("nextAction")
    private NextActionResponse nextAction;

    @SerializedName("shop")
    private ShopResponse shopResponse;

    @SerializedName("customer")
    private CustomerResponse customer;

    @SerializedName("resolution")
    private ResolutionResponse resolution;

    @SerializedName("button")
    private ButtonResponse button;

    @SerializedName("actionBy")
    private int actionBy;

    @SerializedName("conversation")
    private ConversationListResponse conversationList;

    @SerializedName("order")
    private OrderResponse orderResponse;

    @SerializedName("last")
    private LastResponse last;

    public NextActionResponse getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionResponse nextAction) {
        this.nextAction = nextAction;
    }

    public ShopResponse getShopResponse() {
        return shopResponse;
    }

    public void setShopResponse(ShopResponse shopResponse) {
        this.shopResponse = shopResponse;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public ResolutionResponse getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionResponse resolution) {
        this.resolution = resolution;
    }

    public ButtonResponse getButton() {
        return button;
    }

    public void setButton(ButtonResponse button) {
        this.button = button;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public ConversationListResponse getConversationList() {
        return conversationList;
    }

    public void setConversationList(ConversationListResponse conversationList) {
        this.conversationList = conversationList;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    public LastResponse getLast() {
        return last;
    }

    public void setLast(LastResponse last) {
        this.last = last;
    }
}
