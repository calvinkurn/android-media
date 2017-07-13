package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 7/5/17.
 */

public class Conversations {
    @SerializedName("id")
    private String id;
    @SerializedName("message")
    private String message;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("createBy")
    private CreateByX createBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("attachments")
    private List<Attachments> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public CreateByX getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByX createBy) {
        this.createBy = createBy;
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

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }
}
