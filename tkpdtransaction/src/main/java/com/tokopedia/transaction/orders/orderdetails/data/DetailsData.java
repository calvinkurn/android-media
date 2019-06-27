package com.tokopedia.transaction.orders.orderdetails.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsData {
    @SerializedName("orderDetails")
    @Expose

    private OrderDetails orderDetails;

    public DetailsData(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "[DetailsData:{" + orderDetails + "}]";
    }

    public OrderDetails orderDetails() {
        return orderDetails;
    }
}
