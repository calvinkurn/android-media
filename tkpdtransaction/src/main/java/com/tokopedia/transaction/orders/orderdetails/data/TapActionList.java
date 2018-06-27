package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TapActionList {

    @SerializedName("orderDetailTapAction")
    @Expose
    private List<TapActions> tapActionsList;


    public List<TapActions> getTapActionsList() {
        return tapActionsList;
    }

    public void setTapActionsList(List<TapActions> tapActionsList) {
        this.tapActionsList = tapActionsList;
    }

}
