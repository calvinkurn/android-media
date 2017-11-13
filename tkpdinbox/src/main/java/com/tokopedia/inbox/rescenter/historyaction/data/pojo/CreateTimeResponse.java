package com.tokopedia.inbox.rescenter.historyaction.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 13/11/17.
 */

public class CreateTimeResponse {

    @SerializedName("timestampz")
    private String createTimestamp;
    @SerializedName("str")
    private String createTimeStr;
    @SerializedName("time")
    private String createTime;

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
