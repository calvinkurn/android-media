package com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit;

import java.util.List;

/**
 * Created by nisie on 4/4/17.
 */

public class ReplySubmitData {

    private Integer isAppeal;
    private Integer solutionFlag;
    private Integer systemFlag;
    private String problemType;
    private Integer conversationId;
    private String userImg;
    private List<AttachmentData> attachment = null;
    private String createTimeWib;
    private Integer notificationFlag;
    private Integer actionBy;
    private String action;
    private String remarkStr;
    private String userLabel;
    private Integer chatFlag;
    private String userUrl;
    private Integer resetFlag;
    private String createTime;
    private String remark;
    private String userName;
    private Integer userLabelId;
    private String timeAgo;
    private ByData by;

    public Integer getIsAppeal() {
        return isAppeal;
    }

    public void setIsAppeal(Integer isAppeal) {
        this.isAppeal = isAppeal;
    }

    public Integer getSolutionFlag() {
        return solutionFlag;
    }

    public void setSolutionFlag(Integer solutionFlag) {
        this.solutionFlag = solutionFlag;
    }

    public Integer getSystemFlag() {
        return systemFlag;
    }

    public void setSystemFlag(Integer systemFlag) {
        this.systemFlag = systemFlag;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public List<AttachmentData> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentData> attachment) {
        this.attachment = attachment;
    }

    public String getCreateTimeWib() {
        return createTimeWib;
    }

    public void setCreateTimeWib(String createTimeWib) {
        this.createTimeWib = createTimeWib;
    }

    public Integer getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(Integer notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public Integer getActionBy() {
        return actionBy;
    }

    public void setActionBy(Integer actionBy) {
        this.actionBy = actionBy;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemarkStr() {
        return remarkStr;
    }

    public void setRemarkStr(String remarkStr) {
        this.remarkStr = remarkStr;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public Integer getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(Integer chatFlag) {
        this.chatFlag = chatFlag;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public Integer getResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(Integer resetFlag) {
        this.resetFlag = resetFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(Integer userLabelId) {
        this.userLabelId = userLabelId;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public ByData getBy() {
        return by;
    }

    public void setBy(ByData by) {
        this.by = by;
    }
}
