package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/19/17.
 */

public class PaymentEntity {
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("pre_auth_amount")
    @Expose
    private String preAuthAmount;

    @SerializedName("total_amount")
    @Expose
    private String totalAmount;

    public PaymentEntity() {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getPreAuthAmount() {
        return preAuthAmount;
    }
}
