package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 07/11/17.
 */
public class SellerAddressResponse {

    @SerializedName("address")
    private AddressResponse address;
    @SerializedName("by")
    private ByResponse by;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("conversationId")
    private int conversationId;
    @SerializedName("createTimeFullStr")
    private String createTimeFullStr;

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }

    public ByResponse getBy() {
        return by;
    }

    public void setBy(ByResponse by) {
        this.by = by;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
    }
}
