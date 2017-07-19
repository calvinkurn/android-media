package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.apiservices.search.SearchService;

/**
 * Created by alvarisi on 7/10/17.
 */

public class PendingPaymentEntity {
    @SerializedName("pending_amount")
    @Expose
    private String pendingAmount;
    @SerializedName("operator_id")
    @Expose
    private String operatorId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("topup_options")
    @Expose
    private String topUpOptions;
    @SerializedName("topup_url")
    @Expose
    private String topupUrl;

    public String getPendingAmount() {
        return pendingAmount;
    }

    public String getBalance() {
        return balance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTopUpOptions() {
        return topUpOptions;
    }

    public String getTopupUrl() {
        return topupUrl;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
