package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */
public class ResolutionResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private StatusResponse status;
    @SerializedName("read")
    private int read;
    @SerializedName("responded")
    private int responded;
    @SerializedName("createTime")
    private CreateTimeResponse createTime;
    @SerializedName("lastReplyTime")
    private LastReplyTimeResponse lastReplyTime;
    @SerializedName("autoExecuteTime")
    private AutoExecuteTimeResponse autoExecuteTime;
    @SerializedName("freeReturn")
    private int freeReturn;
    @SerializedName("product")
    private List<ProductResponse> product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getResponded() {
        return responded;
    }

    public void setResponded(int responded) {
        this.responded = responded;
    }

    public CreateTimeResponse getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeResponse createTime) {
        this.createTime = createTime;
    }

    public LastReplyTimeResponse getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(LastReplyTimeResponse lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public AutoExecuteTimeResponse getAutoExecuteTime() {
        return autoExecuteTime;
    }

    public void setAutoExecuteTime(AutoExecuteTimeResponse autoExecuteTime) {
        this.autoExecuteTime = autoExecuteTime;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    public List<ProductResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ProductResponse> product) {
        this.product = product;
    }

}
