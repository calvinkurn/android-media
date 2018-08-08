package com.tokopedia.transaction.purchase.detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 11/23/17. Tokopedia
 */

public class OrderDetailResponseError {

    @SerializedName("code:")
    @Expose
    private int code;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("detail")
    @Expose
    private String detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
