package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/14/16.
 */
public class DataRequestSingleAd {
    @SerializedName("action")
    @Expose
    String action;

    @SerializedName("shop_id")
    @Expose
    String shopId;

    @SerializedName("ads")
    List<DataRequestSingleAds> ads = new ArrayList<>();

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

    public List<DataRequestSingleAds> getAds() {
        return ads;
    }

    public void setAds(List<DataRequestSingleAds> ads) {
        this.ads = ads;
    }
}
