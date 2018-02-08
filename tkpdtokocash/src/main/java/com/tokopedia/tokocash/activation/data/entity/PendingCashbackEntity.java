package com.tokopedia.tokocash.activation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class PendingCashbackEntity {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("amount_text")
    @Expose
    private String amountText;
    @SerializedName("currency")
    @Expose
    private String currency;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
