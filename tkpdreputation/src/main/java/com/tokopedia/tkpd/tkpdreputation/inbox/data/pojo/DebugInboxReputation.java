
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DebugInboxReputation {

    @SerializedName("InboxID")
    @Expose
    private int inboxID;
    @SerializedName("ReputationID")
    @Expose
    private int reputationID;
    @SerializedName("UserID")
    @Expose
    private int userID;
    @SerializedName("ShopID")
    @Expose
    private int shopID;
    @SerializedName("Role")
    @Expose
    private int role;
    @SerializedName("InvoiceRefNum")
    @Expose
    private String invoiceRefNum;
    @SerializedName("Status")
    @Expose
    private int status;
    @SerializedName("ReadStatus")
    @Expose
    private int readStatus;
    @SerializedName("SearchKeyword")
    @Expose
    private String searchKeyword;
    @SerializedName("CreateBy")
    @Expose
    private int createBy;
    @SerializedName("CreateTime")
    @Expose
    private String createTime;
    @SerializedName("UpdateBy")
    @Expose
    private UpdateBy updateBy;
    @SerializedName("Updatetime")
    @Expose
    private Updatetime updatetime;
    @SerializedName("ReputationData")
    @Expose
    private ReputationData reputationData;

    public int getInboxID() {
        return inboxID;
    }

    public int getReputationID() {
        return reputationID;
    }

    public int getUserID() {
        return userID;
    }

    public int getShopID() {
        return shopID;
    }

    public int getRole() {
        return role;
    }

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public int getStatus() {
        return status;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public UpdateBy getUpdateBy() {
        return updateBy;
    }

    public Updatetime getUpdatetime() {
        return updatetime;
    }

    public ReputationData getReputationData() {
        return reputationData;
    }
}
