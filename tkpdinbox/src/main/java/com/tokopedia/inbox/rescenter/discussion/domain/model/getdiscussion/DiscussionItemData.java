package com.tokopedia.inbox.rescenter.discussion.domain.model.getdiscussion;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class DiscussionItemData {

    private int resConvId;
    private String solutionRemark;
    private int actionBy;
    private String createTime;
    private String createTimeStr;
    private List<Attachment> attachment;
    private String actionByStr;
    private String createBy;

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

    public void setActionByStr(String actionByStr) {
        this.actionByStr = actionByStr;
    }

    public String getActionByStr() {
        return actionByStr;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
