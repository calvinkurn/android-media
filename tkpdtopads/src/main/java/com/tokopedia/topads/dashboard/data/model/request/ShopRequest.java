package com.tokopedia.topads.dashboard.data.model.request;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.HashMap;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class ShopRequest {

    private String shopId;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        return params;
    }
}