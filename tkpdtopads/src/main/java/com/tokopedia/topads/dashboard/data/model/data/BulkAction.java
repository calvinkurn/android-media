package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class BulkAction {

    @SerializedName("action")
    @Expose
    String action;

    @SerializedName("shop_id")
    @Expose
    String shopId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
