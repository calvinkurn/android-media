package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class Option {

    @SerializedName("pvo_id")
    @Expose
    private int pvoId;
    @SerializedName("v_id")
    @Expose
    private int vId;
    @SerializedName("vu_id")
    @Expose
    private int vuId;
    @SerializedName("vuv_id")
    @Expose
    private int vuvId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("picture")
    @Expose
    private String picture;

    public int getPvoId() {
        return pvoId;
    }

    public void setPvoId(int pvoId) {
        this.pvoId = pvoId;
    }

    public Integer getVId() {
        return vId;
    }

    public void setVId(int vId) {
        this.vId = vId;
    }

    public int getVuId() {
        return vuId;
    }

    public void setVuId(int vuId) {
        this.vuId = vuId;
    }

    public int getVuvId() {
        return vuvId;
    }

    public void setVuvId(int vuvId) {
        this.vuvId = vuvId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}