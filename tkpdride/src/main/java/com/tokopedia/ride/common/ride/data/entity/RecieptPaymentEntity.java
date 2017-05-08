package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RecieptPaymentEntity {
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("preAuthAmount")
    @Expose
    private float preAuthAmount;

    @SerializedName("totalAmount")
    @Expose
    private float totalAmount;

    public RecieptPaymentEntity() {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public float getPreAuthAmount() {
        return preAuthAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }
}
