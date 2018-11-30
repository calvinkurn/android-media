package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderLabelList {

    @SerializedName("labelBhasa")
    @Expose
    String labelBhasa;


    @SerializedName("orderCategory")
    @Expose

    String orderCategory;

    public String getLabelBhasa() {
        return labelBhasa;
    }


    public String getOrderCategory() {
        return orderCategory;
    }


}