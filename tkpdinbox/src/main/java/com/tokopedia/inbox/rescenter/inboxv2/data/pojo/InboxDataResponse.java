package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class InboxDataResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("resolution")
    private ResolutionResponse resolution;
    @SerializedName("shop")
    private ShopResponse shop;
    @SerializedName("customer")
    private CustomerResponse customer;
    @SerializedName("order")
    private OrderResponse order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResolutionResponse getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionResponse resolution) {
        this.resolution = resolution;
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

}
