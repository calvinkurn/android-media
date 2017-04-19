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
    @SerializedName("amount")
    @Expose
    private String amount;

    public PaymentEntity() {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAmount() {
        return amount;
    }
}
