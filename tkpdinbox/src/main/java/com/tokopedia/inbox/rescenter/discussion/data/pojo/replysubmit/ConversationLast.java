
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationLast {

    @SerializedName("is_appeal")
    @Expose
    private Integer isAppeal;
    @SerializedName("solution_flag")
    @Expose
    private Integer solutionFlag;
    @SerializedName("system_flag")
    @Expose
    private Integer systemFlag;
    @SerializedName("problem_type")
    @Expose
    private String problemType;
    @SerializedName("conversation_id")
    @Expose
    private Integer conversationId;
    @SerializedName("user_img")
    @Expose
    private String userImg;
    @SerializedName("attachment")
    @Expose
    private List<Attachment> attachment = null;
    @SerializedName("create_time_wib")
    @Expose
    private String createTimeWib;
    @SerializedName("notification_flag")
    @Expose
    private Integer notificationFlag;
    @SerializedName("action_by")
    @Expose
    private Integer actionBy;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("remark_str")
    @Expose
    private String remarkStr;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("chat_flag")
    @Expose
    private Integer chatFlag;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("reset_flag")
    @Expose
    private Integer resetFlag;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_label_id")
    @Expose
    private Integer userLabelId;
    @SerializedName("time_ago")
    @Expose
    private String timeAgo;
    @SerializedName("by")
    @Expose
    private By_ by;

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

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
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

    public By_ getBy() {
        return by;
    }

    public void setBy(By_ by) {
        this.by = by;
    }

}
