package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;

import java.util.List;

/**
 * Created by hangnadi on 3/17/17.
 */

@SuppressWarnings("ALL")
public class DetailResCenterEntity {

    @SerializedName("nextAction")
    private NextActionResponse nextAction;
    @SerializedName("last")
    private DetailResCenterLast last;
    @SerializedName("button")
    private DetailResCenterButton button;
    @SerializedName("shop")
    private DetailResCenterShop shop;
    @SerializedName("customer")
    private DetailResCenterCustomer customer;
    @SerializedName("order")
    private DetailResCenterOrder order;
    @SerializedName("resolution")
    private DetailResCenterResolution resolution;
    @SerializedName("history")
    private List<DetailResCenterHistory> history;
    @SerializedName("by")
    private DetailResCenterBy by;

    public DetailResCenterLast getLast() {
        return last;
    }

    public void setLast(DetailResCenterLast last) {
        this.last = last;
    }

    public DetailResCenterButton getButton() {
        return button;
    }

    public void setButton(DetailResCenterButton button) {
        this.button = button;
    }

    public DetailResCenterShop getShop() {
        return shop;
    }

    public void setShop(DetailResCenterShop shop) {
        this.shop = shop;
    }

    public DetailResCenterCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(DetailResCenterCustomer customer) {
        this.customer = customer;
    }

    public DetailResCenterOrder getOrder() {
        return order;
    }

    public void setOrder(DetailResCenterOrder order) {
        this.order = order;
    }

    public DetailResCenterResolution getResolution() {
        return resolution;
    }

    public void setResolution(DetailResCenterResolution resolution) {
        this.resolution = resolution;
    }

    public List<DetailResCenterHistory> getHistory() {
        return history;
    }

    public void setHistory(List<DetailResCenterHistory> history) {
        this.history = history;
    }

    public DetailResCenterBy getBy() {
        return by;
    }

    public void setBy(DetailResCenterBy by) {
        this.by = by;
    }

    public NextActionResponse getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionResponse nextAction) {
        this.nextAction = nextAction;
    }
}
