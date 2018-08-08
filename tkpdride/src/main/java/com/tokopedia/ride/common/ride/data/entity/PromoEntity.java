package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/31/17.
 */

public class PromoEntity {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("url")
    @Expose
    private String url;

    public String getCode() {
        return code;
    }

    public String getOffer() {
        return offer;
    }

    public String getUrl() {
        return url;
    }
}
