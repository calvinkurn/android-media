package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterResolution {
    @SerializedName("id")
    private String id;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("updateTime")
    private int updateTime;
    @SerializedName("expireTime")
    private int expireTime;
    @SerializedName("freeReturn")
    private int freeReturn;
    @SerializedName("splitInfo")
    private String splitInfo;
    @SerializedName("expireTimeStr")
    private String expireTimeStr;
    @SerializedName("updateTimeStr")
    private String updateTimeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    public String getSplitInfo() {
        return splitInfo;
    }

    public void setSplitInfo(String splitInfo) {
        this.splitInfo = splitInfo;
    }

    public String getExpireTimeStr() {
        return expireTimeStr;
    }

    public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

}
