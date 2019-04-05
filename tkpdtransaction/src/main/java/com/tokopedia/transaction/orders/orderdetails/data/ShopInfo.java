package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfo {

    @SerializedName("id")
    @Expose
    private int shopId;

    @SerializedName("name")
    @Expose
    private String shopName;

    @SerializedName("logo")
    @Expose
    private String shopLogo;

    @SerializedName("shopURL")
    @Expose
    private String shopUrl;

    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public String getShopUrl() {
        return shopUrl;
    }
}
