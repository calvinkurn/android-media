package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/10/17.
 */

public class RideRequestMapEntity {
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("href")
    @Expose
    private String href;

    public RideRequestMapEntity() {
    }

    public String getRequestId() {
        return requestId;
    }

    public String getHref() {
        return href;
    }
}
