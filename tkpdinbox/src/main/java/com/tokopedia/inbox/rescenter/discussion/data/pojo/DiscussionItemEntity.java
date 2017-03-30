package com.tokopedia.inbox.rescenter.discussion.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class DiscussionItemEntity {

    @SerializedName("resConvId")
    private int resConvId;

    @SerializedName("solutionRemark")
    private String solutionRemark;

    @SerializedName("actionBy")
    private int actionBy;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("createTimeStr")
    private String createTimeStr;

    @SerializedName("attachment")
    private List<Attachment> attachment;

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public String getSolutionRemark() {
        return solutionRemark;
    }

    public void setSolutionRemark(String solutionRemark) {
        this.solutionRemark = solutionRemark;
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

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
        this.attachment = attachment;
    }
}
