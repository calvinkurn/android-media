package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.SerializedName;

public class AutoSweepDetail {
    @SerializedName("mutualfund_account_status")
    private int accountStatus;
    @SerializedName("mutualfund_balance")
    private double balance;
    @SerializedName("autosweep_status")
    private int autoSweepStatus;
    @SerializedName("amount_limit")
    private long amountLimit;
    @SerializedName("text")
    private DetailText text;

    public DetailText getText() {
        return text;
    }

    public void setText(DetailText text) {
        this.text = text;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAutoSweepStatus() {
        return autoSweepStatus;
    }

    public void setAutoSweepStatus(int autoSweepStatus) {
        this.autoSweepStatus = autoSweepStatus;
    }

    public long getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(long amountLimit) {
        this.amountLimit = amountLimit;
    }

    @Override
    public String toString() {
        return "AutoSweepDetail{" +
                "autoSweepLimit=" + accountStatus +
                ", balance=" + balance +
                ", autoSweepStatus=" + autoSweepStatus +
                ", amountLimit=" + amountLimit +
                ", text=" + text +
                '}';
    }
}
