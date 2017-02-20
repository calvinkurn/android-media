package com.tokopedia.seller.topads.domain.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class DataDeposit {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("amount_fmt")
    @Expose
    private String amountFmt;

    /**
     *
     * @return
     * The amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The amountFmt
     */
    public String getAmountFmt() {
        return amountFmt;
    }

    /**
     *
     * @param amountFmt
     * The amount_fmt
     */
    public void setAmountFmt(String amountFmt) {
        this.amountFmt = amountFmt;
    }
}
