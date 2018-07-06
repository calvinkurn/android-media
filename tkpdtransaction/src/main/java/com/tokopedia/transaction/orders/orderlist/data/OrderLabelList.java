package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderLabelList {

    @SerializedName("label")
    @Expose
    String label;


    @SerializedName("orderCategory")
    @Expose

    String orderCategory;

    public String getLabel() {
        return label;
    }


    public String getOrderCategory() {
        return orderCategory;
    }


}
