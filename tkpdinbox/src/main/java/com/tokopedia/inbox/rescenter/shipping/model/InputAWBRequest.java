package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InputAWBRequest {

    @SerializedName("resolution_id")
    @Expose
    private int resId;

    @SerializedName("awb")
    @Expose
    private String awbNum;

    @SerializedName("courier_id")
    @Expose
    private int courierId;

    @SerializedName("attachment_count")
    @Expose
    private int attachmentCount;

    @SerializedName("pictures")
    @Expose
    private List<String> pictures;

    @SerializedName("cache_key")
    @Expose
    private String cacheKey;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getAwbNum() {
        return awbNum;
    }

    public void setAwbNum(String awbNum) {
        this.awbNum = awbNum;
    }

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
