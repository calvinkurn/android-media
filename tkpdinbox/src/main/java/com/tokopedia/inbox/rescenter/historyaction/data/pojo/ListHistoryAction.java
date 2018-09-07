package com.tokopedia.inbox.rescenter.historyaction.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListHistoryAction {
    @SerializedName("resConvId")
    private String resConvId;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("createTime")
    private CreateTimeResponse createTime;
    @SerializedName("actionByStr")
    private String actionByStr;
    @SerializedName("remark")
    private String remark;

    public String getResConvId() {
        return resConvId;
    }

    public void setResConvId(String resConvId) {
        this.resConvId = resConvId;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionByStr() {
        return actionByStr;
    }

    public void setActionByStr(String actionByStr) {
        this.actionByStr = actionByStr;
    }

    public CreateTimeResponse getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeResponse createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
