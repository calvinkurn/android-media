package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {
    @SerializedName("content-type")
    @Expose
    private String contentType;

    @SerializedName("itemLabel")
    @Expose
    private String itemLabel;

    public String getContentType() {
        return contentType;
    }

    public String getItemLabel() {
        return itemLabel;
    }
}
