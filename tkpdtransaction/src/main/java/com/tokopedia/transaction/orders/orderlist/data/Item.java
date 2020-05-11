package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;

    public Item(int id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String name() {
        return name;
    }
}
