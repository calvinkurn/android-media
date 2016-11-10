package com.tokopedia.transaction.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class BankAccount {
    private static final String TAG = BankAccount.class.getSimpleName();

    @SerializedName("bank_id")
    @Expose
    private String bankId;
    @SerializedName("bank_account_id")
    @Expose
    private String bankAccountId;
    @SerializedName("bank_account_name")
    @Expose
    private String bankAccountName;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_account_number")
    @Expose
    private String bankAccountNumber;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public static BankAccount instanceInfo(String info) {
        BankAccount data = new BankAccount();
        data.setBankAccountId("0");
        data.setBankAccountName("");
        data.setBankAccountNumber("");
        data.setBankName(info);
        data.setBankId("0");
        return data;
    }

    @Override
    public String toString() {
        return ((bankId.equals("0") && bankAccountId.equals("0"))
                || (bankId.equals("ADD_NEW") && bankAccountId.equals("ADD_NEW")))
                ? bankName : bankName + " " + bankAccountNumber + " a/n: " + getBankAccountName();
    }

    public static BankAccount instanceAddNew(String info) {
        BankAccount data = new BankAccount();
        data.setBankAccountId("ADD_NEW");
        data.setBankAccountName("");
        data.setBankAccountNumber("");
        data.setBankName(info);
        data.setBankId("ADD_NEW");
        return data;
    }
}
