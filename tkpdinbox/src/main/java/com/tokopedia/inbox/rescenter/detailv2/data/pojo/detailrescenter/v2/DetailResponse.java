package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ButtonResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */

public class DetailResponse {

    @SerializedName("first")
    private FirstResponse first;
    @SerializedName("last")
    private LastResponse last;
    @SerializedName("button")
    private ButtonResponse button;
    @SerializedName("shop")
    private ShopResponse shop;
    @SerializedName("customer")
    private CustomerResponse customer;
    @SerializedName("order")
    private OrderResponse order;
    @SerializedName("resolution")
    private ResolutionResponse resolution;
    @SerializedName("actionBy")
    private ActionByResponse actionBy;
    @SerializedName("nextAction")
    private NextActionResponse nextAction;
    @SerializedName("attachments")
    private List<AttachmentUserResponse> attachments;
    @SerializedName("logs")
    private List<LogResponse> logs;

    public FirstResponse getFirst() {
        return first;
    }

    public void setFirst(FirstResponse first) {
        this.first = first;
    }

    public LastResponse getLast() {
        return last;
    }

    public void setLast(LastResponse last) {
        this.last = last;
    }

    public ButtonResponse getButton() {
        return button;
    }

    public void setButton(ButtonResponse button) {
        this.button = button;
    }

    public ShopResponse getShop() {
        return shop;
    }

    public void setShop(ShopResponse shop) {
        this.shop = shop;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public void setOrder(OrderResponse order) {
        this.order = order;
    }

    public ResolutionResponse getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionResponse resolution) {
        this.resolution = resolution;
    }

    public ActionByResponse getActionBy() {
        return actionBy;
    }

    public void setActionBy(ActionByResponse actionBy) {
        this.actionBy = actionBy;
    }

    public NextActionResponse getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionResponse nextAction) {
        this.nextAction = nextAction;
    }

    public List<LogResponse> getLogs() {
        return logs;
    }

    public void setLogs(List<LogResponse> logs) {
        this.logs = logs;
    }

    public List<AttachmentUserResponse> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentUserResponse> attachments) {
        this.attachments = attachments;
    }
}
