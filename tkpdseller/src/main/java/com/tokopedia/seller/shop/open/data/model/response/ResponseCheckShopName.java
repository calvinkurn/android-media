package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseCheckShopName {

    @SerializedName("shop_name")
    @Expose
    private String shopName = null;

    @SerializedName("shop_name_status")
    @Expose
    private String shopNameStatus = null;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNameStatus() {
        return shopNameStatus;
    }

    public void setShopNameStatus(String shopNameStatus) {
        this.shopNameStatus = shopNameStatus;
    }
}
