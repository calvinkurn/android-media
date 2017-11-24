package com.tokopedia.seller.shop.setting.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendry on 3/22/2017.
 */

public class ResponseUserData {
    @SerializedName("domain")
    @Expose
    private String domain = null;

    @SerializedName("reserve_time")
    @Expose
    private String reserveTime = null;

    @SerializedName("shop_name")
    @Expose
    private String shopName = null;

    @SerializedName("step")
    @Expose
    private String step = null;

    @SerializedName("user_id")
    @Expose
    private String userId = null;

    @SerializedName("tag_line")
    @Expose
    private String tagLine = null;

    @SerializedName("short_desc")
    @Expose
    private String shortDesc = null;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
