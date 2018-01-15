package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.FreeReturnResponse;

/**
 * Created by yfsx on 07/11/17.
 */
public class ResolutionResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private StatusResponse status;
    @SerializedName("createBy")
    private CreateByResponse createBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("createTimeFullStr")
    private String createTimeFullStr;
    @SerializedName("expireTime")
    private String expireTime;
    @SerializedName("expireTimeStr")
    private String expireTimeStr;
    @SerializedName("updateBy")
    private CreateByResponse updateBy;
    @SerializedName("updateTime")
    private String updateTime;
    @SerializedName("freeReturn")
    private int freeReturn;
    @SerializedName("freeReturnText")
    private FreeReturnResponse freeReturnText;

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

    public CreateByResponse getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByResponse createBy) {
        this.createBy = createBy;
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

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getExpireTimeStr() {
        return expireTimeStr;
    }

    public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
    }

    public CreateByResponse getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(CreateByResponse updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    public FreeReturnResponse getFreeReturnText() {
        return freeReturnText;
    }

    public void setFreeReturnText(FreeReturnResponse freeReturnText) {
        this.freeReturnText = freeReturnText;
    }

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
    }
}
