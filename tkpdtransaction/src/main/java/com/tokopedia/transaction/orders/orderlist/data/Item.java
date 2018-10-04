package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;

    public Item(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String name() {
        return name;
    }
}
