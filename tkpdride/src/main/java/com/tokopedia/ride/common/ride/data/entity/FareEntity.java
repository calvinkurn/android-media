package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEntity {
    @SerializedName("fare_id")
    @Expose
    String fareId;
    @SerializedName("expires_at")
    @Expose
    int expiresAt;
    @SerializedName("currency_code")
    @Expose
    String currencyCode;
    @SerializedName("value")
    @Expose
    int value;
    @SerializedName("display")
    @Expose
    String display;

    public FareEntity() {
    }

    public String getFareId() {
        return fareId;
    }

    public int getExpiresAt() {
        return expiresAt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }
}
