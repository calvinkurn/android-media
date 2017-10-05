package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterHistory  {
    @SerializedName("resConvId")
    private String resConvId;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("remark")
    private String remark;
    @SerializedName("actionByStr")
    private String actionByText;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActionByText() {
        return actionByText;
    }

    public void setActionByText(String actionByText) {
        this.actionByText = actionByText;
    }
}
