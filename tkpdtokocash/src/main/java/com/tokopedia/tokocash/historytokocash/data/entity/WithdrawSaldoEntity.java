package com.tokopedia.tokocash.historytokocash.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 10/20/17.
 */

public class WithdrawSaldoEntity {

    @SerializedName("amount")
    @Expose
    private long amount;
    @SerializedName("dest_email")
    @Expose
    private String dest_email;
    @SerializedName("withdrawal_id")
    @Expose
    private int withdrawal_id;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDest_email() {
        return dest_email;
    }

    public void setDest_email(String dest_email) {
        this.dest_email = dest_email;
    }

    public int getWithdrawal_id() {
        return withdrawal_id;
    }

    public void setWithdrawal_id(int withdrawal_id) {
        this.withdrawal_id = withdrawal_id;
    }
}