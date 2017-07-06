package com.tokopedia.inbox.rescenter.discussion.domain.model.reply;

import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ByData;

import java.util.List;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionDomainData {

    private String userImg;
    private String conversationId;
    private int userLabelId;
    private int solutionFlag;
    private String userUrl;
    private String userName;
    private int actionBy;
    private String createTimeWib;
    private String remark;
    private ByData by;
    private String remarkStr;
    private String userLabel;
    private int systemFlag;
    private String createTime;
    private String timeAgo;

    private String postKey;
    private List<ReplyAttachmentDomainData> replyAttachmentDomainData;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    public int getSolutionFlag() {
        return solutionFlag;
    }

    public void setSolutionFlag(int solutionFlag) {
        this.solutionFlag = solutionFlag;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getCreateTimeWib() {
        return createTimeWib;
    }

    public void setCreateTimeWib(String createTimeWib) {
        this.createTimeWib = createTimeWib;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ByData getBy() {
        return by;
    }

    public void setBy(ByData by) {
        this.by = by;
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

    public int getSystemFlag() {
        return systemFlag;
    }

    public void setSystemFlag(int systemFlag) {
        this.systemFlag = systemFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setReplyAttachmentDomainData(List<ReplyAttachmentDomainData> replyAttachmentDomainData) {
        this.replyAttachmentDomainData = replyAttachmentDomainData;
    }

    public List<ReplyAttachmentDomainData> getReplyAttachmentDomainData() {
        return replyAttachmentDomainData;
    }
}
