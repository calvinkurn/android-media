package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 1/23/18.
 */

public class ManualTransferEntity {
    @SerializedName("unique_code")
    @Expose
    private int uniqueCode;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("account_bank_name")
    @Expose
    private String accountBankName;
    @SerializedName("account_branch")
    @Expose
    private String accountBranch;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;

    public ManualTransferEntity() {
    }

    public String getTotal() {
        return total;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public String getAccountBranch() {
        return accountBranch;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public int getUniqueCode() {
        return uniqueCode;
    }
}
