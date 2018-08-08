package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/20/17.
 */

public class PriceDetailEntity {
    /*
    "price_details": {
     "base": 1000,
     "cancellation_fee": 5000,
     "cost_per_distance": 1250,
     "cost_per_minute": 0,
     "currency_code": "IDR",
     "distance_unit": "km",
     "minimum": 5000,
     "service_fees": []
     },*/
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("cancellation_fee")
    @Expose
    private String cancellationFee;
    @SerializedName("cost_per_distance")
    @Expose
    private String costPerDistance;
    @SerializedName("cost_per_minute")
    @Expose
    private String costPerMinute;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("distance_unit")
    @Expose
    private String distanceUnit;
    @SerializedName("minimum")
    @Expose
    private String minimum;
    @SerializedName("service_fees")
    @Expose
    private List<String> serviceFees;

    public PriceDetailEntity() {
    }

    public String getBase() {
        return base;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public String getCostPerDistance() {
        return costPerDistance;
    }

    public String getCostPerMinute() {
        return costPerMinute;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public String getMinimum() {
        return minimum;
    }

    public List<String> getServiceFees() {
        return serviceFees;
    }
}
