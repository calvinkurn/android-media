package com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 23/01/18.
 */

public class RatePerMonth {

    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("amount_idr")
    @Expose
    private String amountIdr;
    @SerializedName("rate")
    @Expose
    private double rate;

    public int getAmount() {
        return amount;
    }

    public String getAmountIdr() {
        return amountIdr;
    }

    public double getRate() {
        return rate;
    }

}