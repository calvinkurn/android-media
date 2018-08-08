package com.tokopedia.session.changephonenumber.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningData {
    @SerializedName("is_success")
    private int isSuccess;
    @SerializedName("warning")
    private List<String> warning;
    @SerializedName("saldo")
    private String saldo;
    @SerializedName("tokocash")
    private String tokocash;
    @SerializedName("saldo_number")
    private long saldoNumber;
    @SerializedName("tokocash_number")
    private long tokocashNumber;
    @SerializedName("action")
    private String action;
    @SerializedName("have_bank_acct")
    private boolean hasBankAccount;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<String> getWarning() {
        return warning;
    }

    public void setWarning(List<String> warning) {
        this.warning = warning;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getTokocash() {
        return tokocash;
    }

    public void setTokocash(String tokocash) {
        this.tokocash = tokocash;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getHasBankAccount() {
        return hasBankAccount;
    }

    public void setHasBankAccount(boolean hasBankAccount) {
        this.hasBankAccount = hasBankAccount;
    }

    public long getSaldoNumber() {
        return saldoNumber;
    }

    public void setSaldoNumber(long saldoNumber) {
        this.saldoNumber = saldoNumber;
    }

    public long getTokocashNumber() {
        return tokocashNumber;
    }

    public void setTokocashNumber(long tokocashNumber) {
        this.tokocashNumber = tokocashNumber;
    }

}
