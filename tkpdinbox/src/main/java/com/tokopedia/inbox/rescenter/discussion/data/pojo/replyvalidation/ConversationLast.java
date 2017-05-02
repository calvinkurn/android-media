
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationLast {

    @SerializedName("user_img")
    @Expose
    private String userImg;
    @SerializedName("conversation_id")
    @Expose
    private int conversationId;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("solution_flag")
    @Expose
    private int solutionFlag;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("action_by")
    @Expose
    private int actionBy;
    @SerializedName("create_time_wib")
    @Expose
    private String createTimeWib;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("is_appeal")
    @Expose
    private int isAppeal;
    @SerializedName("problem_type")
    @Expose
    private String problemType;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("by")
    @Expose
    private By by;
    @SerializedName("notification_flag")
    @Expose
    private int notificationFlag;
    @SerializedName("remark_str")
    @Expose
    private String remarkStr;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("chat_flag")
    @Expose
    private int chatFlag;
    @SerializedName("system_flag")
    @Expose
    private int systemFlag;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("reset_flag")
    @Expose
    private int resetFlag;
    @SerializedName("time_ago")
    @Expose
    private String timeAgo;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getIsAppeal() {
        return isAppeal;
    }

    public void setIsAppeal(int isAppeal) {
        this.isAppeal = isAppeal;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public By getBy() {
        return by;
    }

    public void setBy(By by) {
        this.by = by;
    }

    public int getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(int notificationFlag) {
        this.notificationFlag = notificationFlag;
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

    public int getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(int chatFlag) {
        this.chatFlag = chatFlag;
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

    public int getResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(int resetFlag) {
        this.resetFlag = resetFlag;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

}
