package com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore;

import java.util.List;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreItemData {

    private int resConvId;
    private String solutionRemark;
    private int actionBy;
    private String createTime;
    private String createTimeStr;
    private List<LoadMoreAttachment> attachment;
    private String actionByStr;

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

    public List<LoadMoreAttachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<LoadMoreAttachment> attachment) {
        this.attachment = attachment;
    }

    public void setActionByStr(String actionByStr) {
        this.actionByStr = actionByStr;
    }

    public String getActionByStr() {
        return actionByStr;
    }
}
