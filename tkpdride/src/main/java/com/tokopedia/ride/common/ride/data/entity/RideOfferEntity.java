package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideOfferEntity {
    @SerializedName("html")
    @Expose
    private String html;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("terms")
    @Expose
    private String terms;

    public RideOfferEntity() {
    }

    public String getHtml() {
        return html;
    }

    public String getUrl() {
        return url;
    }

    public String getTerms() {
        return terms;
    }
}
