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

    @SerializedName("receipt_ready")
    @Expose
    private boolean receiptReady;

    @SerializedName("pending_amount")
    @Expose
    private String pendingAmount;

    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;

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

    public boolean isReceiptReady() {
        return receiptReady;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }
}
