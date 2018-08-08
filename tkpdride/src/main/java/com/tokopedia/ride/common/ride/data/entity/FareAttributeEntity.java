package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 5/3/17.
 */

public class FareAttributeEntity {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("DetailId")
    @Expose
    private String detailId;
    @SerializedName("DetailEn")
    @Expose
    private String detailEn;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getDetailId() {
        return detailId;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
