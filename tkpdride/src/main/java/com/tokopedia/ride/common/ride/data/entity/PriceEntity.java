package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 7/17/17.
 */

public class PriceEntity {
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("estimate")
    @Expose
    private String estimate;
    @SerializedName("high_estimate")
    @Expose
    private int highEstimate;
    @SerializedName("low_estimate")
    @Expose
    private int lowEstimate;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("localized_display_name")
    @Expose
    private String localizedDisplayName;
    @SerializedName("product_id")
    @Expose
    private String productId;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDistance() {
        return distance;
    }

    public String getEstimate() {
        return estimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public int getLowEstimate() {
        return lowEstimate;
    }

    public String getLocalizedDisplayName() {
        return localizedDisplayName;
    }

    public String getProductId() {
        return productId;
    }

    public int getDuration() {
        return duration;
    }
}
